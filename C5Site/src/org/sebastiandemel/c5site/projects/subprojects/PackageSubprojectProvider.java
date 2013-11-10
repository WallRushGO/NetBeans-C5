/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sebastiandemel.c5site.projects.subprojects;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.SubprojectProvider;
import org.openide.filesystems.FileObject;
import org.sebastiandemel.c5site.projects.SiteProject;

/**
 *
 * @author Sebastian
 */
public class PackageSubprojectProvider implements SubprojectProvider {
    
    private final SiteProject project;

    public PackageSubprojectProvider(SiteProject project) {
        this.project = project;
    }

    @Override
    public Set getSubprojects() {
        return loadProjects(project.getProjectDirectory());
    }

    private Set loadProjects(FileObject dir) {
        Set newProjects = new HashSet();
        try {
            FileObject[] subFolders = dir.getChildren(); //subfolders of root folder
            
            for (FileObject oneSub : subFolders) {
                if (oneSub.getName().equals("packages")) {
                    
                    FileObject[] packageFolder = oneSub.getChildren();
                    
                    for (FileObject onePackage : packageFolder) {
                        
                        Project subp = ProjectManager.getDefault().findProject(onePackage);
                        if (subp != null && subp instanceof PackageSubprojectProvider) {
                            newProjects.add((PackageSubprojectProvider) subp);
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
        return Collections.unmodifiableSet(newProjects);
    }

    @Override
    public final void addChangeListener(ChangeListener l) {}
    
    @Override
    public final void removeChangeListener(ChangeListener l) { }
}
