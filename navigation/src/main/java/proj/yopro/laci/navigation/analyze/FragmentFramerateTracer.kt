package proj.yopro.laci.navigation.analyze

import android.app.Activity
import android.util.Log
import androidx.core.app.FrameMetricsAggregator
import androidx.core.util.size
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import proj.yopro.laci.navigation.BuildConfig

class FragmentFramerateTracer(
    private val scope: LifecycleCoroutineScope, // from : lifecycle-runtime-ktx
    private val tag: String = "SampleFragmentName",
) : CoroutineScope by scope {
    // a mutex lock for thread safe
    private val mutex = Mutex()

    // collect all the metrics
    private val aggregator: FrameMetricsAggregator = FrameMetricsAggregator()

    private var totalFrames = 0L
    private var slowFrames = 0L
    private var frozenFrames = 0L

    fun start(context: Activity) =
        launch {
            Log.d(tag, "/tracer/start")
            mutex.withLock {
                aggregator.add(context)
            }
        }

    // Fragment should call scope.stop when it's hidden
    fun stop() =
        launch {
            Log.d(tag, "/tracer/stop")
            mutex.withLock {
                val data = aggregator.stop() ?: return@launch
                totalFrames = 0L
                slowFrames = 0L
                frozenFrames = 0L

                data[FrameMetricsAggregator.TOTAL_INDEX].let { distributions ->
                    for (i in 0 until distributions.size) {
                        val duration = distributions.keyAt(i)
                        val frameCount = distributions.valueAt(i)
                        totalFrames += frameCount
                        if (duration > 16) {
                            // takes more than 16ms: considered slow
                            slowFrames += frameCount
                        }
                        if (duration > 700) {
                            // takes more than 700ms: considered frozen
                            frozenFrames += frameCount
                        }
                    }
                }
                aggregator.reset()
                val frameRateData = FrameRateData(totalFrames, slowFrames, frozenFrames)
                if (BuildConfig.DEBUG) Log.d(tag, "\n/tracer/result : \n$frameRateData")
            }
        }
}

fun MyBaseFragment.registerTracer() {
    val tag = "Tracer${this::class.java.simpleName}"
    var tracer: FragmentFramerateTracer? = null

    val onHiddenChangedListener =
        object : OnHiddenChangedListener {
            override fun onHidden(hidden: Boolean) {
                if (!hidden) {
                    // this fragment is showing
                    if (tracer == null) {
                        // if tracer is null, set one
                        tracer = FragmentFramerateTracer(lifecycleScope, tag)
                    }
                    tracer?.start(context as Activity)
                } else {
                    // this fragment is not visible
                    tracer?.stop()
                }
            }
        }

    lifecycle.addObserver(
        object : LifecycleEventObserver {
            override fun onStateChanged(
                source: LifecycleOwner,
                event: Lifecycle.Event,
            ) {
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        tracer = FragmentFramerateTracer(lifecycleScope, tag)
                        tracer?.start(context as Activity)
                        addOnHiddenChangedListener(onHiddenChangedListener)
                    }

                    Lifecycle.Event.ON_STOP -> {
                        tracer?.stop()
                        tracer = null
                        removeOnHiddenChangedListener(onHiddenChangedListener)
                    }

                    Lifecycle.Event.ON_DESTROY -> {
                        lifecycle.removeObserver(this)
                    }

                    else -> { // handle those else
                    }
                }
            }
        },
    )
}

class FrameRateData(
    private val frames: Long = 0,
    private val slow: Long = 0,
    private val frozen: Long = 0,
) {
    override fun toString(): String =
        """
        totalFrames : $frames,
        slowFrames  : $slow,
        frozenFrames: $frozen
        """.trimIndent()
}
