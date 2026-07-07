package com.example.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.data.model.Task
import com.example.data.model.SubjectProgress
import com.example.data.model.McqQuestion
import com.example.data.model.MockTestResult
import com.example.data.model.Note
import com.example.data.model.CurrentAffairItem
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Tasks
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

    // Subject Progress
    @Query("SELECT * FROM subject_progress")
    fun getAllSubjectProgress(): Flow<List<SubjectProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjectProgress(progress: List<SubjectProgress>)

    @Update
    suspend fun updateSubjectProgress(progress: SubjectProgress)

    // MCQs
    @Query("SELECT * FROM mcqs")
    fun getAllMcqs(): Flow<List<McqQuestion>>

    @Query("SELECT * FROM mcqs WHERE subjectId = :subjectId")
    fun getMcqsBySubject(subjectId: String): Flow<List<McqQuestion>>

    @Query("SELECT * FROM mcqs WHERE isBookmarked = 1")
    fun getBookmarkedMcqs(): Flow<List<McqQuestion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMcqs(mcqs: List<McqQuestion>)

    @Update
    suspend fun updateMcq(mcq: McqQuestion)

    // Mock Test Results
    @Query("SELECT * FROM mock_test_results ORDER BY dateMillis DESC")
    fun getAllMockTestResults(): Flow<List<MockTestResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMockTestResult(result: MockTestResult)

    // Notes
    @Query("SELECT * FROM notes ORDER BY dateCreated DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%'")
    fun searchNotes(query: String): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNoteById(id: Int)

    // Current Affairs
    @Query("SELECT * FROM current_affairs ORDER BY id DESC")
    fun getAllCurrentAffairs(): Flow<List<CurrentAffairItem>>

    @Query("SELECT * FROM current_affairs WHERE isBookmarked = 1")
    fun getBookmarkedCurrentAffairs(): Flow<List<CurrentAffairItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentAffairs(items: List<CurrentAffairItem>)

    @Update
    suspend fun updateCurrentAffair(item: CurrentAffairItem)
}

@Database(
    entities = [
        Task::class,
        SubjectProgress::class,
        McqQuestion::class,
        MockTestResult::class,
        Note::class,
        CurrentAffairItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
