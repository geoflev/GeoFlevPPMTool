package io.geoflev.ppmtool.services;

import io.geoflev.ppmtool.domain.Backlog;
import io.geoflev.ppmtool.domain.Project;
import io.geoflev.ppmtool.exceptions.ProjectIdException;
import io.geoflev.ppmtool.repositories.BacklogRepository;
import io.geoflev.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project){
        try{
            //catch duplicates in database
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            //new project
            if(project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            //update without backlog being set to null
            if(project.getId() != null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project Id '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
        }
    }

    public Project findByProjectIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId);
        if(project == null){
            throw new ProjectIdException("Project Id '" + projectId+ "' does not exist");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = findByProjectIdentifier(projectId.toUpperCase());
        if(project == null){
            throw new ProjectIdException("Cannot delete project with id '" + projectId +"'.This project does not exist");
        }
        projectRepository.delete(project);
    }
}
