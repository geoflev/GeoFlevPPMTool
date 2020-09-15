package io.geoflev.ppmtool.services;

import io.geoflev.ppmtool.domain.Backlog;
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

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask,String username){

            Backlog backlog = projectService.findByProjectIdentifier(projectIdentifier, username).getBacklog();
            projectTask.setBacklog(backlog);
            Integer backlogSequence = backlog.getPTSequence();
            backlogSequence++;
            backlog.setPTSequence(backlogSequence);
            projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + backlogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if(projectTask.getPriority() == 0 ||  projectTask.getPriority() == null){
                projectTask.setPriority(3);
            }

            //if u check for "" first it wont check for null
            if(projectTask.getStatus() == null || projectTask.getStatus() == ""){
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);

        //we created a custom exception by creating the ProjectNotFoundExceptionResponse entity
        //after that, the ProjectNotFound
        //and at last we added those to the CustomResponseEntity
        //so that we can throw it here
    }

    public Iterable<ProjectTask> findBacklogById(String id, String username) {

        projectService.findByProjectIdentifier(id,username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username){

        //make sure we are searching in an existing backlog
        projectService.findByProjectIdentifier(backlog_id,username);

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

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username){

        //instead of repeating myself i use the above method
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTask = updatedTask;
        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username){

        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);

//        manually deleting because relationship wasn't working
//        we were getting 200 OK on delete but it wasn't deleting
//        we had CascadeType.REFRESH on the wrong side of the relationship
//        should be on Backlog side
//        and we added orphanRemoval = true

        projectTaskRepository.delete(projectTask);
    }
}


















