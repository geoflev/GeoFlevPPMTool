package io.geoflev.ppmtool.services;

import io.geoflev.ppmtool.domain.Backlog;
import io.geoflev.ppmtool.domain.ProjectTask;
import io.geoflev.ppmtool.repositories.BacklogRepository;
import io.geoflev.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        //WHAT WE WANT HERE
        //PTs to be added to a specific project, not null project, backlog exists
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        //set the backlog to the PT
        projectTask.setBacklog(backlog);
        //projectSequence = projectIdentifier-task within the project!!!!!!!!!!!!!!!!!!!!!!!!
        //e.g. IDPRO-1, IDPRO-2
        Integer backlogSequence = backlog.getPTSequence();
        backlogSequence++;
        backlog.setPTSequence(backlogSequence);
        //Add sequence to project task
        //update the backlog sequence
        projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + backlogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        if(projectTask.getPriority() == null){
            projectTask.setPriority(3);
        }

        if(projectTask.getStatus() == "" || projectTask.getStatus() == null){
            projectTask.setStatus("TO_DO");
        }

        return projectTaskRepository.save(projectTask);
    }

    public Iterable<ProjectTask> findBacklogById(String id) {
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }
}
