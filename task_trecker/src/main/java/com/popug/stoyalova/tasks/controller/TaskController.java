package com.popug.stoyalova.tasks.controller;

import com.popug.stoyalova.tasks.dto.TaskDto;
import com.popug.stoyalova.tasks.model.Task;
import com.popug.stoyalova.tasks.model.TaskModel;
import com.popug.stoyalova.tasks.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskDto> getTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String publicId = authentication.getName();
            return taskService.findAllByUserPublicId(publicId).stream().map(this :: entityMapper).collect(Collectors.toList());
        }
        return List.of();
    }
    private TaskDto entityMapper(Task entity){
        return TaskDto.builder()
                .title(entity.getTitle())
                .creationDate(entity.getCreateDate())
                .description(entity.getDescription())
                .publicId(entity.getPublicId())
                .status(entity.getStatus().name())
                .userCreated(entity.getUserCreate().getPublicId())
                .userAssign(entity.getUserAssign().getPublicId())
                .build();
    }

    @PostMapping("/create")
    public Map<String, Object> save(@RequestBody TaskModel taskModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String publicId = authentication.getName();
            TaskDto taskDto = TaskDto.builder()
                    .userCreated(publicId)
                    .title(taskModel.getTitle())
                    .description(taskModel.getDescription())
                    .build();
            String savedId = taskService.save(taskDto);
            return Map.of("id", savedId);
        }
        return Map.of("id", "ERROR");
    }

    @PutMapping("/{taskPublicId}")
    public void close(@PathVariable String taskPublicId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String publicId = authentication.getName();
            TaskDto taskDto = TaskDto.builder()
                    .userAssign(publicId)
                    .publicId(taskPublicId)
                    .build();
            taskService.update(taskDto);
        }
    }

    @GetMapping("/reassign")
    public void reassign(){
        taskService.reassign();
    }
}
