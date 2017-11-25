package xyz.b515.schedule.ui.view

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.view.Menu
import android.view.MenuItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_course_manage.*
import kotlinx.android.synthetic.main.app_bar.*
import org.jetbrains.anko.startActivity
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import xyz.b515.schedule.Constant
import xyz.b515.schedule.R
import xyz.b515.schedule.api.ZfRetrofit
import xyz.b515.schedule.db.CourseManager
import xyz.b515.schedule.ui.adapter.CourseAdapter
import xyz.b515.schedule.util.CourseParser
import xyz.b515.schedule.util.FileHelper
import java.io.File
import java.io.FileInputStream
import java.util.*

@RuntimePermissions
class CourseManageActivity : AppCompatActivity() {
    private val FILE_SELECT_CODE = 0

    lateinit var adapter: CourseAdapter
    private val manager: CourseManager by lazy { CourseManager(this) }
    private var progressDialog: ProgressDialog? = null
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_manage)
        toolbar.setTitle(R.string.course_manage)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        fab.setOnClickListener { startActivity<CourseDetailActivity>(Constant.TOOLBAR_TITLE to true) }

        adapter = CourseAdapter(ArrayList())
        recycler.adapter = adapter
        recycler.itemAnimator = DefaultItemAnimator()
    }

    override fun onResume() {
        super.onResume()
        loadCourses()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_course_manage, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_clear -> {
                manager.clearCourse()
                loadCourses()
            }
            R.id.action_import -> {
                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                getCourses(prefs.getString("user", null), prefs.getString("password", null))
            }
            R.id.action_import_file -> {
                showFileChooserWithPermissionCheck()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadCourses() {
        adapter.items.clear()
        adapter.items.addAll(manager.getAllCourse())
        adapter.notifyDataSetChanged()
    }

    private fun getCourses(user: String, password: String) {
        val d = disposable
        d?.let { if (!d.isDisposed) d.dispose() }

        val loginMap = HashMap<String, String>()
        loginMap.put("TextBox1", user)
        loginMap.put("TextBox2", password)
        loginMap.put("RadioButtonList1_2", "%D1%A7%C9%FA")
        loginMap.put("Button1", "")

        val scheduleMap = HashMap<String, String>()
        scheduleMap.put("xh", user)
        scheduleMap.put("gnmkdm", "N121603")

        val zfService = ZfRetrofit.zfService
        disposable = zfService.login(loginMap)
                .flatMap {
                    //TODO check login state
                    zfService.getSchedule(scheduleMap)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ text ->
                    manager.clearCourse()
                    CourseParser.parse(text, manager)
                    loadCourses()
                }, { throwable ->
                    dismissProgressDialog()
                    Snackbar.make(recycler, "Error!!!" + throwable.message, Snackbar.LENGTH_LONG).show()
                    throwable.printStackTrace()
                }, { dismissProgressDialog() }
                ) { sub -> progressDialog = ProgressDialog.show(this@CourseManageActivity, "Schedule", "Now loading...", true, true) { sub.cancel() } }
    }

    override fun onStop() {
        super.onStop()
        dismissProgressDialog()
        val d = disposable
        d?.let { if (!d.isDisposed) d.dispose() }
    }

    private fun dismissProgressDialog() {
        val p = progressDialog
        p?.let { if (p.isShowing) p.dismiss() }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    internal fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(Intent.createChooser(intent, ""), FILE_SELECT_CODE)
        } catch (e: Exception) {
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FILE_SELECT_CODE -> if (resultCode == Activity.RESULT_OK && data != null) {
                try {
                    val uri = data.data
                    val path = FileHelper.getPath(this, uri)
                    val file = File(path)
                    val input = FileInputStream(file)
                    val length = file.length().toInt()
                    val temp = ByteArray(length)
                    input.read(temp, 0, length)
                    val text = String(temp, charset("gb2312"))
                    input.close()

                    manager.clearCourse()
                    CourseParser.parse(text, manager)
                    loadCourses()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
