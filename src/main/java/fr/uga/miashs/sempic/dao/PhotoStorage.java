/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.dao;

import fr.uga.miashs.sempic.SempicException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletContext;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@ApplicationScoped
public class PhotoStorage {
    private static Logger logger = Logger.getLogger(PhotoStorage.class.getName());

    private Path pictureStore;
    private Path thumbnailStore;

    public PhotoStorage() throws IOException {
        this(Paths.get("PhotoStore"), Paths.get("ThumbnailStore"));
    }

    public PhotoStorage(Path pictureStore, Path thumbnailStore) throws IOException {
        this.pictureStore = pictureStore;
        this.thumbnailStore = thumbnailStore;
        if (Files.notExists(pictureStore)) {
            Files.createDirectories(pictureStore);
        }
        if (Files.notExists(thumbnailStore)) {
            Files.createDirectories(thumbnailStore);
        }
        logger.log(Level.INFO, "Picture store location: {0}", pictureStore.toRealPath().toString());
        logger.log(Level.INFO, "Thumbnail store location: {0}", thumbnailStore.toRealPath().toString());
    }

    @Inject
    public PhotoStorage(ServletContext context) throws IOException {
        this(Paths.get(context.getInitParameter("PhotoStorePath")), Paths.get(context.getInitParameter("ThumbnailStorePath")));
    }

    // Normalize the path and check that we do not go before pictureStore
    // for security reasons
    // i.e. we do not allow /path/to/store + ../../ddsdd
    private static Path buildAndVerify(Path root, Path rel) throws SempicException {
        Path res = root;
        res = res.resolve(rel).normalize();
        if (!res.startsWith(root)) {
            throw new SempicException("Invalid path");
        }
        return res;
    }

    public void savePicture(Path p, InputStream data) throws SempicException {

        Path loc = buildAndVerify(pictureStore, p);

        try {
            Files.createDirectories(loc.getParent());
            Files.copy(data, loc, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new SempicException("Failed to copy the photo", ex);
        }
    }

    public void deletePicture(Path picPath) throws SempicException {
        Path loc = buildAndVerify(pictureStore, picPath);

        try {
            if (Files.deleteIfExists(loc)) {

                if (!Files.newDirectoryStream(loc.getParent()).iterator().hasNext()) {
                    Files.delete(loc.getParent());
                }

                Files.walk(thumbnailStore).filter(p -> p.endsWith(picPath)).forEach(p -> {
                    try {
                        Files.delete(p);
                        if (!Files.newDirectoryStream(p.getParent()).iterator().hasNext()) {
                            Files.delete(p.getParent());
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(PhotoStorage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

            }
        } catch (IOException ex) {
            throw new SempicException("Failed to copy the photo", ex);
        }
    }
    
    public Path getPicturePath(Path p) throws SempicException {
        Path picPath = buildAndVerify(pictureStore, p);
        if (Files.notExists(picPath)) {
            throw new SempicException("The picture " + p + " does not exists");
        }
        return picPath;
    }

    public Path getThumbnailPath(Path p, int width) throws SempicException, IOException {
        Path thumbnailPath = buildAndVerify(thumbnailStore.resolve(String.valueOf(width)), p);
        if (Files.notExists(thumbnailPath)) {
            Path picPath = getPicturePath(p);
            Path parent = thumbnailPath.getParent();
            if (Files.notExists(parent)) {
                Files.createDirectories(parent);
            }
            BufferedImage bim = ImageIO.read(picPath.toFile());
            int height = (int) (bim.getHeight() * (((double) width) / bim.getWidth()));
            Image resizedImg = bim.getScaledInstance(width, height, Image.SCALE_FAST);
            BufferedImage rBimg = new BufferedImage(width, height, bim.getType());
            // Create Graphics object
            Graphics2D g = rBimg.createGraphics();

            // Draw the resizedImg from 0,0 with no ImageObserver
            g.drawImage(resizedImg, 0, 0, null);

            // Dispose the Graphics object, we no longer need it
            g.dispose();
            Files.createFile(thumbnailPath);
            ImageIO.write(rBimg, "png", thumbnailPath.toFile());

        }
        return thumbnailPath;
    }

    public void emptyCache() throws IOException {
        Files.walk(thumbnailStore).filter(p -> Files.isRegularFile(p)).forEach(p -> {
            try {
                Files.delete(p);
            } catch (IOException ex) {
                Logger.getLogger(PhotoStorage.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

}