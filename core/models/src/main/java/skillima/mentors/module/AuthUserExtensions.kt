package skillima.mentors.module

fun AuthUser.isMentorProfileComplete(): Boolean {
    return bio.isNotBlank() &&
        githubUrl.isNotBlank() &&
        linkedinUrl.isNotBlank() &&
        xUrl.isNotBlank()
}
