package todokt.controller

import org.springframework.web.bind.annotation.*
import todokt.service.TodoService

@RestController
@RequestMapping("/todo")
class TodoApiController(
    private val todoService: TodoService
) {

    @GetMapping
    fun getTodos() = todoService.getTodos()

    @GetMapping(path = ["/{todoId}"])
    fun getTodo(@PathVariable todoId: Long) = todoService.getTodoById(todoId)

    @PostMapping
    fun insertTodo(@RequestBody todoRequestDto: TodoRequestDto) = todoService.insertTodo(todoRequestDto.todoName)

    @PutMapping(path = ["/{todoId}"])
    fun updateTodo(@PathVariable todoId: Long) = todoService.updateTodo(todoId)

    @DeleteMapping(path = ["/{todoId}"])
    fun deleteTodo(@PathVariable todoId: Long) = todoService.deleteTodo(todoId)
}