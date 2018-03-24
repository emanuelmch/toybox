package bill.catbox.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import bill.catbox.R
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), HomeView {

    private val presenter = HomePresenter(this)

    override val boxChosenEvent = BehaviorSubject.create<Int>()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupButtons()
    }

    override fun onResume() {
        super.onResume()
        presenter.attach()
    }

    override fun onPause() {
        presenter.detach()
        super.onPause()
    }


    private fun setupButtons() {
        this.box1.setOnClickListener { boxChosenEvent.onNext(0) }
        this.box2.setOnClickListener { boxChosenEvent.onNext(1) }
        this.box3.setOnClickListener { boxChosenEvent.onNext(2) }
        this.box4.setOnClickListener { boxChosenEvent.onNext(3) }
        this.box5.setOnClickListener { boxChosenEvent.onNext(4) }
    }

}
