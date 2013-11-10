package org.sebastiandemel.c5site.projects;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.sebastiandemel.c5site.projects.subprojects.PackageSubprojectProvider;
import org.sebastiandemel.c5site.views.SiteProjectLogicalView;

public class SiteProject implements Project {
    private final FileObject projectDir;
    private final ProjectState state;
    private Lookup lkp;

    public SiteProject(FileObject dir, ProjectState state) {
        this.projectDir = dir;
        this.state = state;
    }

    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    @Override
    public Lookup getLookup() {
        if (lkp == null) {
            lkp = Lookups.fixed(new Object[]{ 

                new Info(),
                new SiteProjectLogicalView(this),
                new PackageSubprojectProvider(this), //Package Subprojects of this project
            });
        }
        return lkp;
    }
    
    private final class Info implements ProjectInformation {
        @StaticResource()
        public static final String SITE_ICON = "org/sebastiandemel/c5site/icon_site_generic.png";

        //Found from: http://www.mkyong.com/java/how-to-resize-an-image-in-java/
        private BufferedImage resizeImage(BufferedImage originalImage, int type){
            BufferedImage resizedImage = new BufferedImage(16, 16, type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, 16, 16, null);
            g.dispose();
            g.setComposite(AlphaComposite.Src);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

            return resizedImage;
        }
        
        @Override
        public Icon getIcon() {
            
            FileObject iconFile = getProjectDirectory().getFileObject("icon.png");
            ImageIcon projectIcon = null;
              
            if (iconFile != null) {
                String iconPath = iconFile.getPath();
                try {
                    //Read image data
                    BufferedImage iconImage = ImageIO.read(iconFile.getInputStream());
                    
                    //Get the image type and resize the image
                    int type = iconImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : iconImage.getType();
                    iconImage = this.resizeImage(iconImage, type);
                    
                    projectIcon = new ImageIcon(iconImage);
                    iconImage = null;
                }catch(IOException exeption) {
                    projectIcon =  new ImageIcon(ImageUtilities.loadImage(SITE_ICON, true));
                }
            } else {
                projectIcon =  new ImageIcon(ImageUtilities.loadImage(SITE_ICON, true));
            }
            
            return projectIcon;
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public Project getProject() {
            return SiteProject.this;
        }

    }
}
