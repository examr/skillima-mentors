package skillima.data.guild.repository

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.filter.TextSearchType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import skillima.data.guild.model.GuildResponse
import skillima.data.guild.model.GuildWithSkills
import skillima.data.guild.model.SkillResponseDto
import skillima.data.guild.utils.mapToNetworkError
import skillima.data.guild.utils.toDto
import skillima.mentors.module.GuildDto
import skillima.mentors.module.SkillDto
import skillima.mentors.utils.Response

class GuildRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : GuildRepository {

    override suspend fun fetchGuilds(
        offset: Long,
        limit: Long
    ): Flow<Response<List<GuildDto>>> = flow {
        emit(Response.Loading)

        try {
            val json = Json { ignoreUnknownKeys = true }

            val raw = supabaseClient.postgrest
                .rpc(
                    "get_guilds_with_skills",
                    parameters = buildJsonObject {
                        put("_limit", limit)
                        put("_offset", offset)
                    }
                )
                .data

            val response = json.decodeFromString<GuildResponse>(raw)
            val guildDtos = response.guilds.map {
                it.copy(totalCount = response.totalCount).toDto()
            }

            emit(Response.Success(guildDtos))
        } catch (e: Exception) {
            Log.e("TAG", "Guild Repo: ${e.message}")
            emit(Response.Error(mapToNetworkError(e)))
        }
    }


    override suspend fun fetchSkills(
        offset: Long,
        limit: Long
    ): Flow<Response<List<SkillDto>>> = flow {
        emit(Response.Loading)

        try {
            val skills = supabaseClient.postgrest["skills"].select {
                range(offset, offset + limit - 1)
            }
                .decodeList<SkillDto>()

            emit(Response.Success(skills))
        } catch (e: Exception) {
            Log.e("TAG", "Fetch Skills Error: ${e.message}")
            emit(Response.Error(mapToNetworkError(e)))
        }
    }


    override suspend fun saveUserSkills(
        userId: String,
        skillIds: List<String>,
        proficiency: String
    ): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)
        try {
            supabaseClient.postgrest
                .rpc(
                    "add_user_skills",
                    parameters = buildJsonObject {
                        put("_user_id", userId)
                        putJsonArray("_skill_ids") { skillIds.forEach { add(it) } }
                        put("_proficiency", proficiency)
                    }
                )

            Log.i("TAG", "Save Skills Success")
            emit(Response.Success(true))
        } catch (e: Exception) {
            Log.e("TAG", "Save Skills Error: ${e.message}")
            emit(Response.Error(mapToNetworkError(e)))
        }
    }

    override suspend fun searchSkills(
        search: String,
        offset: Long,
        limit: Long
    ): Flow<Response<List<SkillDto>>> = flow {
        emit(Response.Loading)
        try {
            val skills = supabaseClient.postgrest
                .rpc(
                    "search_skills",
                    parameters = buildJsonObject {
                        put("_search", search.trim())
                        put("_offset", offset)
                        put("_limit", limit)
                    }
                )
                .decodeList<SkillDto>()

            emit(Response.Success(skills))
        } catch (e: Exception) {
            Log.e("TAG", "Search Skills Error: ${e.message}")
            emit(Response.Error(mapToNetworkError(e)))
        }
    }

    override suspend fun searchGuilds(
        search: String,
        offset: Long,
        limit: Long
    ): Flow<Response<List<GuildDto>>> = flow {
        emit(Response.Loading)
        try {
            val json = Json { ignoreUnknownKeys = true }

            val raw = supabaseClient.postgrest
                .rpc(
                    "search_guilds",
                    parameters = buildJsonObject {
                        put("_search", search.trim())
                        put("_offset", offset)
                        put("_limit", limit)
                    }
                )
                .data

            val response = json.decodeFromString<GuildResponse>(raw)
            val guildDtos = response.guilds.map {
                it.copy(totalCount = response.totalCount).toDto()
            }

            emit(Response.Success(guildDtos))
        } catch (e: Exception) {
            Log.e("TAG", "Search Guilds Error: ${e.message}")
            emit(Response.Error(mapToNetworkError(e)))
        }
    }
}