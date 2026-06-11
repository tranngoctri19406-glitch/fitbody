package com.example.fitbody.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fitbody.model.CartItem
import com.example.fitbody.model.CheckIn
import com.example.fitbody.model.Product
import com.example.fitbody.model.Schedule
import com.example.fitbody.model.Trainer
import com.example.fitbody.model.Workout
import com.example.fitbody.model.WorkoutStatsResponse

class DatabaseHelper(context: Context) :

    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "fitbody.db"
        private const val DATABASE_VERSION = 1

        // Table names
        const val TABLE_USERS = "tbl_users"
        const val TABLE_TRAINERS = "tbl_trainers"
        const val TABLE_WORKOUTS = "tbl_workouts"
        const val TABLE_SCHEDULE = "tbl_schedule"
        const val TABLE_PROGRESS = "tbl_progress"
        const val TABLE_CHECKIN = "tbl_checkin"
        const val TABLE_FAVORITES = "tbl_favorites"
        const val TABLE_PRODUCTS = "products"
        const val TABLE_CART = "cart"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create tables
        db.execSQL(
            """
            CREATE TABLE $TABLE_USERS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT,
                password TEXT,
                email TEXT,
                social_id TEXT,
                provider TEXT,
                role TEXT DEFAULT 'user'
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_TRAINERS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                specialty TEXT,
                muscle TEXT,
                calories TEXT,
                schedule_text TEXT,
                image TEXT,
                description TEXT,
                status TEXT DEFAULT 'active'
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_WORKOUTS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                trainer_id INTEGER,
                workout_name TEXT,
                sets_count TEXT,
                reps_count TEXT,
                muscle_group TEXT,
                video_url TEXT,
                FOREIGN KEY(trainer_id) REFERENCES $TABLE_TRAINERS(id)
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_SCHEDULE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                day_name TEXT,
                workout_plan TEXT,
                is_completed INTEGER DEFAULT 0
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_CHECKIN (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                checkin_date TEXT,
                qr_code TEXT
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_FAVORITES (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                trainer_id INTEGER
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_PROGRESS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                weight REAL,
                height REAL,
                bmi REAL,
                date TEXT
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_PRODUCTS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                price INTEGER,
                image TEXT,
                description TEXT,
                category TEXT
            )
        """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_CART (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                product_id INTEGER,
                quantity INTEGER DEFAULT 1,
                FOREIGN KEY(product_id) REFERENCES $TABLE_PRODUCTS(id)
            )
        """.trimIndent()
        )

        // Seed data
        seedTrainers(db)
        seedWorkouts(db)
        seedProducts(db)
    }

    private fun seedTrainers(db: SQLiteDatabase) {
        val trainers = arrayOf(
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (1, 'HLV AN', 'Tăng cơ toàn thân', 'Ngực - Tay sau', '850 kcal', 'Thứ 2 / 4 / 6', 'http://10.0.2.2/fitbody_api/uploads/1779709471_ChatGPT Image 18_44_04 25 thg 5, 2026.png', 'PT chuyên bodybuilding giúp tăng cơ nhanh và siết body.', 'active')",
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (2, 'HLV Quỳnh Anh', 'Fitness nữ', 'Mông - Đùi', '720 kcal', 'Thứ 3 / 5 / 7', 'http://10.0.2.2/fitbody_api/uploads/1779709347_1779708789438_7000948680681510670_7000948680681510670_87700e2435e58f3d91fe5d51910de496.jpg', 'PT nữ chuyên body fitness và tăng vòng 3.', 'active')",
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (16, 'HLV Tiến', 'gym', 'ngực, chân', '800 kcal', 'hằng ngày ', '../uploads/1781077446_HTB10.X2S6DpK1RjSZFrq6y78VXaV.webp', 'kinh nghiệm ', 'active')",
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (17, 'HLV Trí', 'gym', 'tay,vai', '850kcal', 'hằng ngày ', '../uploads/1781078168_7e351380453af12f45573248f4d41048.jpg', 'đào tạo lâu năm ', 'active')",
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (18, 'HLV Nhi', 'gym', 'mông,đùi', '550kcal', 'hằng ngày ', '../uploads/1781078260_bo-do-the-thao-tap-gym-nu2_6d575b51350441019e13ddbc2ae024b3_grande.jpg', 'dáng chuẩn', 'active')",
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (19, 'HLV tony', 'gym', 'full body', '800kcal', 'hằng ngày ', '../uploads/1781078348_006B6279-133D-4CAA-B60A-4E79389B1DC7.webp', 'kinh nghiệm lâu năm', 'active')",
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (21, 'HLV jenny', 'cardio', 'mông, đùi, eo', '700kcal', 'hằng ngày ', '../uploads/1781078516_0ec77b7f4851f0d4f15413af8294c6b9.jpg', 'kinh nghiệm', 'active')",
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (22, 'HLV Minh Anh', 'Fitness nữ', 'Mông - Đùi', '700 kcal', 'Thứ 2 / 4 / 6', 'http://10.0.2.2/fitbody_api/uploads/1781103718_Anh-Gymer-16-min-585x878.jpg', 'PT nữ chuyên tăng vòng 3 và giảm mỡ.', 'active')",
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (23, 'HLV Bảo Ngọc', 'Yoga', 'Toàn thân', '500 kcal', 'Thứ 3 / 5 / 7', 'http://10.0.2.2/fitbody_api/uploads/1781105730_Anh-Gymer-11-min.jpg', 'Huấn luyện Yoga và cải thiện vóc dáng.', 'active')",
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (24, 'HLV Hoàng Nam', 'Bodybuilding', 'Ngực - Vai', '900 kcal', 'Thứ 2 / 4 / 6', 'http://10.0.2.2/fitbody_api/uploads/1781105009_OIP.webp', 'PT chuyên tăng cơ và siết cơ thể.', 'active')",
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (25, 'HLV Quốc Huy', 'Gym', 'Full Body', '850 kcal', 'Thứ 3 / 5 / 7', 'http://10.0.2.2/fitbody_api/uploads/1781106059_OIP (1).webp', 'Chuyên giáo án cho người mới tập gym.', 'active')",
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (26, 'HLV Kim Chi', 'Fitness nữ', 'Bụng - Eo', '650 kcal', 'Thứ 2 / 5 / 7', 'http://10.0.2.2/fitbody_api/uploads/1781105823_kim-ye-eun-v0-5azjpayi96fa1.webp', 'PT nữ chuyên giảm mỡ bụng và giữ dáng.', 'active')",
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (27, 'HLV Tuấn Kiệt', 'Bodybuilding', 'Tay - Vai', '950 kcal', 'Hàng ngày', 'http://10.0.2.2/fitbody_api/uploads/1781106127_Anh-Gymer-9-min-585x390.jpg', 'Chuyên tăng cơ cho nam giới.', 'active')",
            "INSERT INTO $TABLE_TRAINERS (id, name, specialty, muscle, calories, schedule_text, image, description, status) VALUES (28, 'HLV Lan Hương', 'Cardio', 'Toàn thân', '600 kcal', 'Thứ 2 / 3 / 5', 'http://10.0.2.2/fitbody_api/uploads/1781105890_7763449db84b68e52a55a57fa7524ca4.jpg', 'PT nữ chuyên giảm cân và cardio.', 'active')"
        )
        for (query in trainers) {
            db.execSQL(query)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRAINERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WORKOUTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SCHEDULE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CHECKIN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROGRESS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    private fun seedProducts(db: SQLiteDatabase) {
        val products = arrayOf(
            "INSERT INTO $TABLE_PRODUCTS (name, price, image, description, category) VALUES ('Whey Protein Isolate', 1200000, 'whey_gold.png', 'Hỗ trợ tăng cơ bắp nhanh chóng.', 'Supplement')",
            "INSERT INTO $TABLE_PRODUCTS (name, price, image, description, category) VALUES ('BCAA 5000', 850000, 'bcaa.png', 'Phục hồi cơ bắp trong lúc tập.', 'Supplement')",
            "INSERT INTO $TABLE_PRODUCTS (name, price, image, description, category) VALUES ('Creatine Monohydrate', 450000, 'creatine.png', 'Tăng sức mạnh và sức bền.', 'Supplement')",
            "INSERT INTO $TABLE_PRODUCTS (name, price, image, description, category) VALUES ('Găng tay tập Gym', 250000, 'gloves.png', 'Bảo vệ lòng bàn tay.', 'Accessory')",
            "INSERT INTO $TABLE_PRODUCTS (name, price, image, description, category) VALUES ('Thảm tập Yoga', 350000, 'yoga_mat.png', 'Chống trơn trượt hiệu quả.', 'Accessory')"
        )
        for (query in products) {
            db.execSQL(query)
        }
    }

    private fun seedWorkouts(db: SQLiteDatabase) {
        val workouts = arrayOf(
            // HLV AN (ID: 1)
            "INSERT INTO $TABLE_WORKOUTS (id, trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (1, 1, 'Bench Press', '4 sets', '12 reps', 'Ngực', 'https://youtu.be/rT7DgCr-3pg')",
            "INSERT INTO $TABLE_WORKOUTS (id, trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (2, 1, 'Incline Dumbbell Press', '4 sets', '10 reps', 'Ngực', 'https://youtu.be/rT7DgCr-3pg')",
            "INSERT INTO $TABLE_WORKOUTS (id, trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (3, 1, 'Cable Fly', '3 sets', '15 reps', 'Ngực', 'https://youtu.be/rT7DgCr-3pg')",
            "INSERT INTO $TABLE_WORKOUTS (id, trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (7, 1, 'Tricep Pushdown', '3 sets', '12 reps', 'Tay sau', '')",

            // HLV Quỳnh Anh (ID: 2)
            "INSERT INTO $TABLE_WORKOUTS (id, trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (4, 2, 'Squat', '5 sets', '15 reps', 'Chân', 'https://youtu.be/aclHkVaku9U')",
            "INSERT INTO $TABLE_WORKOUTS (id, trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (5, 2, 'Hip Thrust', '4 sets', '12 reps', 'Chân', 'https://youtu.be/aclHkVaku9U')",
            "INSERT INTO $TABLE_WORKOUTS (id, trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (6, 2, 'Bulgarian Split Squat', '3 sets', '12 reps', 'Chân', 'https://youtu.be/aclHkVaku9U')",
            "INSERT INTO $TABLE_WORKOUTS (id, trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (8, 2, 'Lunges', '3 sets', '15 reps', 'Mông', '')",

            // HLV Tiến (ID: 16)
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (16, 'Push Up', '3 sets', '20 reps', 'Ngực', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (16, 'Leg Press', '4 sets', '12 reps', 'Chân', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (16, 'Dumbbell Fly', '3 sets', '12 reps', 'Ngực', '')",

            // HLV Trí (ID: 17)
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (17, 'Bicep Curl', '4 sets', '12 reps', 'Tay trước', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (17, 'Hammer Curl', '3 sets', '12 reps', 'Tay trước', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (17, 'Shoulder Press', '4 sets', '10 reps', 'Vai', '')",

            // HLV Nhi (ID: 18)
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (18, 'Deadlift', '4 sets', '10 reps', 'Mông đùi', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (18, 'Glute Bridge', '4 sets', '20 reps', 'Mông', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (18, 'Sumo Squat', '3 sets', '15 reps', 'Mông đùi', '')",

            // HLV Tony (ID: 19)
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (19, 'Pull Up', '3 sets', '10 reps', 'Lưng', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (19, 'Lat Pulldown', '4 sets', '12 reps', 'Lưng', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (19, 'Seated Row', '3 sets', '12 reps', 'Lưng', '')",

            // HLV Jenny (ID: 21)
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (21, 'Burpees', '3 sets', '15 reps', 'Toàn thân', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (21, 'Mountain Climber', '3 sets', '30s', 'Toàn thân', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (21, 'Plank', '3 sets', '60s', 'Bụng', '')",

            // HLV Minh Anh (ID: 22)
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (22, 'Leg Extension', '4 sets', '15 reps', 'Đùi', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (22, 'Leg Curl', '4 sets', '15 reps', 'Đùi sau', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (22, 'Calf Raise', '3 sets', '20 reps', 'Bắp chân', '')",

            // HLV Bảo Ngọc (ID: 23)
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (23, 'Cobra Pose', '3 sets', '30s', 'Toàn thân', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (23, 'Warrior Pose', '3 sets', '30s', 'Toàn thân', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (23, 'Child Pose', '3 sets', '60s', 'Toàn thân', '')",

            // HLV Hoàng Nam (ID: 24)
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (24, 'Military Press', '4 sets', '10 reps', 'Vai', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (24, 'Lateral Raise', '3 sets', '15 reps', 'Vai', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (24, 'Front Raise', '3 sets', '12 reps', 'Vai', '')",

            // HLV Quốc Huy (ID: 25)
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (25, 'Bodyweight Squat', '3 sets', '20 reps', 'Chân', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (25, 'Plank Hole', '3 sets', '45s', 'Bụng', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (25, 'Jumping Jacks', '3 sets', '30 reps', 'Toàn thân', '')",

            // HLV Kim Chi (ID: 26)
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (26, 'Crunches', '4 sets', '20 reps', 'Bụng', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (26, 'Leg Raise', '3 sets', '15 reps', 'Bụng', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (26, 'Russian Twist', '3 sets', '30 reps', 'Bụng', '')",

            // HLV Tuấn Kiệt (ID: 27)
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (27, 'Skull Crusher', '4 sets', '12 reps', 'Tay sau', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (27, 'Dips', '3 sets', '15 reps', 'Tay sau', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (27, 'Close Grip Bench Press', '3 sets', '10 reps', 'Tay sau', '')",

            // HLV Lan Hương (ID: 28)
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (28, 'High Knees', '3 sets', '45s', 'Toàn thân', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (28, 'Jump Squat', '3 sets', '15 reps', 'Toàn thân', '')",
            "INSERT INTO $TABLE_WORKOUTS (trainer_id, workout_name, sets_count, reps_count, muscle_group, video_url) VALUES (28, 'Bicycle Crunch', '3 sets', '20 reps', 'Bụng', '')"
        )
        for (query in workouts) {
            db.execSQL(query)
        }
    }

    // Helper functions to get Trainers
    fun getAllTrainers(): List<Trainer> {
        val list = mutableListOf<Trainer>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TRAINERS WHERE status = 'active'", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Trainer(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("specialty")),
                        cursor.getString(cursor.getColumnIndexOrThrow("muscle")),
                        cursor.getString(cursor.getColumnIndexOrThrow("calories")),
                        cursor.getString(cursor.getColumnIndexOrThrow("schedule_text")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun addFavorite(userId: Int, trainerId: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("user_id", userId)
            put("trainer_id", trainerId)
        }
        val result = db.insert(TABLE_FAVORITES, null, values)
        return result != -1L
    }

    fun getWorkoutsByTrainer(trainerId: Int): List<Workout> {
        val list = mutableListOf<Workout>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_WORKOUTS WHERE trainer_id = ?",
            arrayOf(trainerId.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Workout(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("trainer_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("workout_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("sets_count")),
                        cursor.getString(cursor.getColumnIndexOrThrow("reps_count")),
                        cursor.getString(cursor.getColumnIndexOrThrow("muscle_group")),
                        cursor.getString(cursor.getColumnIndexOrThrow("video_url"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    // User Authentication
    fun registerUser(username: String, email: String, password: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", username)
            put("email", email)
            put("password", password)
            put("role", "user")
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun checkUser(username: String, password: String): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id FROM $TABLE_USERS WHERE username = ? AND password = ?",
            arrayOf(username, password)
        )
        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0)
        }
        cursor.close()
        return userId
    }

    fun getUserBySocialId(socialId: String, provider: String): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id FROM $TABLE_USERS WHERE social_id = ? AND provider = ?",
            arrayOf(socialId, provider)
        )
        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0)
        }
        cursor.close()
        return userId
    }

    fun registerSocialUser(
        username: String,
        email: String,
        socialId: String,
        provider: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", username)
            put("email", email)
            put("social_id", socialId)
            put("provider", provider)
            put("role", "user")
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun getUserProfile(userId: Int): android.database.Cursor {
        val db = readableDatabase
        return db.rawQuery(
            "SELECT username, email FROM $TABLE_USERS WHERE id = ?",
            arrayOf(userId.toString())
        )
    }

    fun updateUserProfile(userId: Int, name: String, email: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", name)
            put("email", email)
        }
        return db.update(TABLE_USERS, values, "id = ?", arrayOf(userId.toString())) > 0
    }

    // Products
    fun getAllProducts(): List<Product> {
        val list = mutableListOf<Product>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_PRODUCTS", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("category"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    // Cart
    fun addToCart(userId: Int, productId: Int): Boolean {
        val db = writableDatabase
        // Check if product already in cart
        val cursor = db.rawQuery(
            "SELECT quantity FROM $TABLE_CART WHERE user_id = ? AND product_id = ?",
            arrayOf(userId.toString(), productId.toString())
        )

        return if (cursor.moveToFirst()) {
            val newQty = cursor.getInt(0) + 1
            val values = ContentValues().apply { put("quantity", newQty) }
            db.update(
                TABLE_CART,
                values,
                "user_id = ? AND product_id = ?",
                arrayOf(userId.toString(), productId.toString())
            ) > 0
        } else {
            val values = ContentValues().apply {
                put("user_id", userId)
                put("product_id", productId)
                put("quantity", 1)
            }
            db.insert(TABLE_CART, null, values) != -1L
        }.also { cursor.close() }
    }

    fun getCart(userId: Int): List<CartItem> {
        val list = mutableListOf<CartItem>()
        val db = readableDatabase
        val query = """
            SELECT c.id, c.product_id, c.quantity, p.name, p.price, p.image 
            FROM $TABLE_CART c 
            JOIN $TABLE_PRODUCTS p ON c.product_id = p.id 
            WHERE c.user_id = ?
        """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    CartItem(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getInt(2)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    // Favorites
    fun getFavorites(userId: Int): List<Trainer> {
        val list = mutableListOf<Trainer>()
        val db = readableDatabase
        val query = """
            SELECT t.* FROM $TABLE_TRAINERS t 
            JOIN $TABLE_FAVORITES f ON t.id = f.trainer_id 
            WHERE f.user_id = ?
        """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Trainer(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("specialty")),
                        cursor.getString(cursor.getColumnIndexOrThrow("muscle")),
                        cursor.getString(cursor.getColumnIndexOrThrow("calories")),
                        cursor.getString(cursor.getColumnIndexOrThrow("schedule_text")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun removeFavorite(userId: Int, trainerId: Int): Boolean {
        val db = writableDatabase
        return db.delete(
            TABLE_FAVORITES,
            "user_id = ? AND trainer_id = ?",
            arrayOf(userId.toString(), trainerId.toString())
        ) > 0
    }

    // Schedule
    fun getSchedule(userId: Int): List<Schedule> {
        val list = mutableListOf<Schedule>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_SCHEDULE WHERE user_id = ?",
            arrayOf(userId.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Schedule(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("day_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("workout_plan")),
                        if (cursor.getInt(cursor.getColumnIndexOrThrow("is_completed")) == 1) "completed" else "pending"
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun addSchedule(userId: Int, dayName: String, workoutPlan: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("user_id", userId)
            put("day_name", dayName)
            put("workout_plan", workoutPlan)
            put("is_completed", 0)
        }
        return db.insert(TABLE_SCHEDULE, null, values)
    }

    fun completeSchedule(id: Int): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply { put("is_completed", 1) }
        return db.update(TABLE_SCHEDULE, values, "id = ?", arrayOf(id.toString())) > 0
    }

    fun deleteSchedule(id: Int): Boolean {
        val db = writableDatabase
        return db.delete(TABLE_SCHEDULE, "id = ?", arrayOf(id.toString())) > 0
    }

    // Check-in
    fun addCheckIn(userId: Int, qrCode: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("user_id", userId)
            put("qr_code", qrCode)
            put(
                "checkin_date",
                java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
                    .format(java.util.Date())
            )
        }
        return db.insert(TABLE_CHECKIN, null, values)
    }

    fun getCheckInHistoryList(userId: Int): List<CheckIn> {
        val list = mutableListOf<CheckIn>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_CHECKIN WHERE user_id = ? ORDER BY id DESC",
            arrayOf(userId.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    CheckIn(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("checkin_date"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    // Workout Stats
    fun getWorkoutStats(userId: Int): WorkoutStatsResponse {
        val db = readableDatabase

        // Total Workouts (Count check-ins)
        val cursor1 = db.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_CHECKIN WHERE user_id = ?",
            arrayOf(userId.toString())
        )
        var totalWorkouts = 0
        if (cursor1.moveToFirst()) totalWorkouts = cursor1.getInt(0)
        cursor1.close()

        // Dummy stats for others
        return WorkoutStatsResponse(
            success = true,
            total_workouts = totalWorkouts,
            total_calories = totalWorkouts * 350, // Ước tính 350 kcal mỗi buổi
            streak_days = if (totalWorkouts > 0) totalWorkouts % 7 + 1 else 0,
            month_progress = (totalWorkouts * 100 / 20).coerceAtMost(100) // Mục tiêu 20 buổi/tháng
        )
    }

    // Body Progress
    fun saveProgress(userId: Int, weight: Double, height: Double, bmi: Double): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("user_id", userId)
            put("weight", weight)
            put("height", height)
            put("bmi", bmi)
            put(
                "date",
                java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                    .format(java.util.Date())
            )
        }
        return db.insert(TABLE_PROGRESS, null, values)
    }

    fun getLatestProgress(userId: Int): android.database.Cursor {
        val db = readableDatabase
        return db.rawQuery(
            "SELECT * FROM $TABLE_PROGRESS WHERE user_id = ? ORDER BY id DESC LIMIT 1",
            arrayOf(userId.toString())
        )
    }

    fun updateTrainerImage(trainerId: Int, imagePath: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("image", imagePath)
        }
        return db.update(TABLE_TRAINERS, values, "id = ?", arrayOf(trainerId.toString())) > 0
    }
}
