package todokt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.jdbc.Sql
import todokt.controller.TodoRequestDto
import todokt.repository.Todo
import todokt.repository.TodoRepository
import todokt.service.TodoService

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class TodoApiControllerTests
@Autowired constructor(
    private val restTemplate: TestRestTemplate,
    private val todoService: TodoService,
    private val todoRepository: TodoRepository,
    env: Environment
) {
    companion object {
        @BeforeAll
        @Sql("/data.sql")
        fun init() {
        }
    }

    private val port: String = env.getProperty("local.server.port") ?: "8080"
    private val url = "http://localhost:${port}/todo"

    @Test
    fun create() {
        //given
        val todoName = "스프링 부트를 코틀린으로 개발해보자"
        val requestDto = TodoRequestDto(todoName)

        //when
        val responseEntity: ResponseEntity<Long> = restTemplate.postForEntity(url, requestDto, Long::class.java)

        //then
        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntity.body).isGreaterThan(0L)

        val all = todoRepository.findAll().toList()
        assertThat(all[3].todoName).isEqualTo(todoName)
    }

    @Test
    fun readAll() {
        val responseEntity = restTemplate.getForEntity(url, List::class.java)

        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntity.body?.size ?: -1).isGreaterThan(0)
    }

    @Test
    fun readOne() {
        val responseObject = restTemplate.getForObject("${url}/1", Todo::class.java)

        assertThat(responseObject.id).isEqualTo(1)
        assertThat(responseObject.todoName).isEqualTo("샘플데이터1")
    }

    @Test
    fun update() {
        restTemplate.put("${url}/2", null)

        val todo = restTemplate.getForObject("${url}/2", Todo::class.java)
        assertThat(todo.completed).isTrue
    }

    @Test
    fun delete() {
        val sizeBeforeDelete = todoService.getTodos().size
        restTemplate.delete("${url}/3")
        assertThat(todoService.getTodos().size).isLessThan(sizeBeforeDelete)
    }

}
