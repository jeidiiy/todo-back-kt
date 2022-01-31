package todokt.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import todokt.repository.*

@Transactional
@Service
class TodoService(
    private val todoRepository: TodoRepository
) {
    fun getTodos() = todoRepository.findAll().toList()

    fun getTodoById(todoId: Long) =
        todoRepository.findByIdOrNull(todoId) ?: throw IllegalArgumentException("Not found todo with ${todoId}")

    fun insertTodo(todoName: String): Long = todoRepository.save(Todo(todoName = todoName)).id ?: -1

    fun updateTodo(todoId: Long): Todo {
        val todo = todoRepository.findByIdOrNull(todoId) ?: throw Exception()
        todo.completed = !todo.completed
        return todoRepository.save(todo)
    }

    fun deleteTodo(todoId: Long) = todoRepository.deleteById(todoId)

}