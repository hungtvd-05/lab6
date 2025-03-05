package com.example.lab6

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab6.ui.theme.FirebaseAppTheme
import com.google.firebase.firestore.CollectionReference

import com.google.firebase.firestore.FirebaseFirestore

class CourseDetailsActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseAppTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var courseList = mutableStateListOf<Course?>()

                    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

                    db.collection("Courses").get()
                        .addOnSuccessListener {
                            queryDocumentSnapshot ->
                            if (!queryDocumentSnapshot.isEmpty) {
                                val list = queryDocumentSnapshot.documents

                                for (d in list) {
                                    val c: Course? = d.toObject(Course::class.java)
                                    courseList.add(c)
                                }
                            } else {
                                Toast.makeText(this@CourseDetailsActivity, "No data found in Database", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@CourseDetailsActivity, "Failed to get data", Toast.LENGTH_SHORT).show()
                        }

                    firebaseUI(LocalContext.current, courseList)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun firebaseUI(context: Context, courseList: SnapshotStateList<Course?>) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            itemsIndexed(courseList) {
                index, item ->
                Card(
                    onClick = {
                        Toast.makeText(context, courseList[index]?.courseName, Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .padding(8.dp),
//                    elevation = 6.dp
                ) {
                    Column (
                        modifier = Modifier.padding(8.dp).fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.width(5.dp))

                        courseList[index]?.courseName?.let {
                            Text(
                                text = it,
                                modifier = Modifier.padding(4.dp),
                                color = Color.Green,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        courseList[index]?.courseDuration?.let {
                            Text(
                                text = it,
                                modifier = Modifier.padding(4.dp),
//                                color = Color.Green,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = 20.sp,
//                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        courseList[index]?.courseDescription?.let {
                            Text(
                                text = it,
                                modifier = Modifier.padding(4.dp),
//                                color = Color.Green,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
                                    fontSize = 20.sp,
//                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
    }
}

fun addDataToFirebase(
    courseName: String, courseDuration: String, courseDescription: String, context: Context
) {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val dbCourses: CollectionReference = db.collection("Courses")

    val courses = Course(courseName, courseDuration, courseDescription)

    dbCourses.add(courses).addOnSuccessListener {
        Toast.makeText(context, "Your course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show()
    }
        .addOnFailureListener {e ->
            Toast.makeText(context, "Failed to add course \n$e", Toast.LENGTH_SHORT).show()

    }
}