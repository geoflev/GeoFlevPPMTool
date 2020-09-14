package io.geoflev.ppmtool.services;

import io.geoflev.ppmtool.domain.Backlog;
import io.geoflev.ppmtool.domain.Project;
import io.geoflev.ppmtool.domain.ProjectTask;
import io.geoflev.ppmtool.exceptions.ProjectNotFoundException;
import io.geoflev.ppmtool.repositories.BacklogRepository;
import io.geoflev.ppmtool.repositories.ProjectRepository;
import io.geoflev.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        try{
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);
            projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if(projectTask.getPriority() == 0 ||  projectTask.getPriority() == null){
                projectTask.setPriority(3);
            }

            if(projectTask.getStatus() == "" || projectTask.getStatus() == null){
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);
        }catch (Exception e){
            throw new ProjectNotFoundException("Project not found!");
        }
        //we created a custom exception by creating the ProjectNotFoundExceptionResponse entity
        //after that, the ProjectNotFound
        //and at last we added those to the CustomResponseEntity
        //so that we can throw it here
    }

    public Iterable<ProjectTask> findBacklogById(String id) {

        Project project = projectRepository.findByProjectIdentifier(id);
        if(project == null){
            throw new ProjectNotFoundException("Project with ID: '" + id + "' does not exist!");
        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id){

        //make sure we are searching in an existing backlog
        Backlog backlog = backlogRepository.findByProjectIdentifier((backlog_id));
        if(backlog == null){
            throw new ProjectNotFoundException("Project with ID: '" + backlog_id + "' does not exist!");
        }
        //make sure that this task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask == null){
            throw new ProjectNotFoundException("Project task '" + pt_id + "' not found!");
        }
        //make sure that the backlog/project_id in the path corresponds to the right project
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+ pt_id+ "' does not exist in project '"+backlog_id);
        }


        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id){

        //instead of repeating myself i use the above method
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);
        projectTask = updatedTask;
        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id){

        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id);

//        manually deleting because relationship wasn't working
//        we were getting 200 OK on delete but it wasn't deleting
//        we had CascadeType.REFRESH on the wrong side of the relationship
//        should be on Backlog side
//        and we added orphanRemoval = true

        projectTaskRepository.delete(projectTask);
    }
}


















