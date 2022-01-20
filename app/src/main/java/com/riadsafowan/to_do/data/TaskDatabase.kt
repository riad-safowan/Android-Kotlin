package com.riadsafowan.to_do.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.riadsafowan.to_do.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    class CallBack @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            //database operation
//            val dao = database.get().taskDao()
//            applicationScope.launch {
//                dao.insert(Task(taskName = "learning RecyclerView", isCompleted = true))
//                dao.insert(Task(taskName = "learning coroutine"))
//                dao.insert(Task(taskName = "learning Hilt", isImportant = true))
//                dao.insert(Task(taskName = "learning RxJava"))
//                dao.insert(Task(taskName = "learning Retrofit2", isImportant = true))
//                dao.insert(Task(taskName = "learning MVVM", isImportant = true))
//                dao.insert(Task(taskName = "learning kotlin", isImportant = true))
//                dao.insert(Task(taskName = "learning RecyclerView", isCompleted = true))
//                dao.insert(Task(taskName = "learning coroutine"))
//                dao.insert(Task(taskName = "learning Hilt", isImportant = true))
//                dao.insert(Task(taskName = "learning RxJava"))
//                dao.insert(Task(taskName = "learning Retrofit2", isImportant = true))
//                dao.insert(Task(taskName = "learning MVVM", isImportant = true))
//                dao.insert(Task(taskName = "learning kotlin", isImportant = true))
//            }

        }
    }
}