package skillima.mentors.module

data class UserData(
    val userId: String="",
    val name:String="",
    val email:String="",
    val password:String="",
    val role: Role = Role.Mentor
)

enum class Role(val value: String) {
    Student("student"),
    Mentor("mentor")
}
