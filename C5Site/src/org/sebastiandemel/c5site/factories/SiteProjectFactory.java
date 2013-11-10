/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sebastiandemel.c5site.factories;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Enumeration;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectFactory2;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.ServiceProvider;
import org.sebastiandemel.c5site.projects.SiteProject;

/**
 *
 * @author Sebastian
 */

@ServiceProvider(service=ProjectFactory.class)
public class SiteProjectFactory implements ProjectFactory2 {

    public static final String PROJECT_DIR = "texts";
    
    private boolean checkProject(FileObject projectDirectory) {
        //Check is there:
        //- a concrete folder
        //- index.php
        //- a config folder with a config.php file
        boolean found = false;
        
        //Check is there a concrete folder
        FileObject checkObject = projectDirectory.getFileObject("concrete");
        found = (checkObject != null && checkObject.isFolder()) ? true : false;
        
        //Check is there a config folder
        checkObject = projectDirectory.getFileObject("config");
        found = (checkObject != null && checkObject.isFolder()) ? true : false;
        
        //If we found a config fodler check is there a config.php
        if (found) {
            FileObject configFile = checkObject.getFileObject("config.php");
            found = (checkObject != null) ? true : false;
        }
        
        return found;
    }
    
    //Specifies when a project is a project
    @Override
    public boolean isProject(FileObject projectDirectory) {
       return this.checkProject(projectDirectory);
    }
    
    //Specifies when a project is a project
    @Override
    public ProjectManager.Result isProject2(FileObject projectDirectory) {
                boolean result = this.checkProject(projectDirectory);
        
        if (result) {
            ImageIcon projectIcon =  new ImageIcon(ImageUtilities.loadImage(
                    "org/sebastiandemel/c5site/icon_site_generic.png", true));
            return new ProjectManager.Result(projectIcon);
        }
        
        return null;
    }

    //Specifies when the project will be opened, i.e., if the project exists:
    @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        return isProject(dir) ? new SiteProject(dir, state) : null;
    }

    @Override
    public void saveProject(final Project project) throws IOException, ClassCastException {
        // leave unimplemented for the moment
    }
}
