package co.netguru.baby.monitor.client.feature.machinelearning

import android.content.Context
import co.netguru.baby.monitor.client.feature.machinelearning.AacRecorder.Companion.SAMPLING_RATE
import io.reactivex.Single
import org.tensorflow.contrib.android.TensorFlowInferenceInterface
import timber.log.Timber

class MachineLearning(context: Context) {

    private val inferenceInterface = TensorFlowInferenceInterface(
            context.assets,
            "tiny_conv_dataset.pb"
    )
    private val sampleRateList = intArrayOf(SAMPLING_RATE)

    fun processData(array: ShortArray) = Single.just(array).map { data ->
        val outputScores = FloatArray(OUTPUTS_NUMBER)
        val mappedData = FloatArray(DATA_SIZE) {
            if (data.size < it) return@FloatArray 0f

            return@FloatArray if (data[it] >= 0) {
                data[it].toFloat() / Short.MAX_VALUE
            } else {
                data[it].toFloat() / (Short.MIN_VALUE) * -1
            }
        }
        with(inferenceInterface) {
            feed(SAMPLE_RATE_NAME, sampleRateList)
            feed(INPUT_DATA_NAME, mappedData, DATA_SIZE.toLong(), 1)
            run(outputScoresNames, true)
            fetch(OUTPUT_SCORES_NAME, outputScores)
        }
        return@map mapData(outputScores.toTypedArray())
    }

    private fun mapData(floats: Array<Float>): MutableMap<String, Float> {
        val map = mutableMapOf<String, Float>()
        map[OUTPUT_1_SILENCE] = floats[0]
        map[OUTPUT_2_BACKGROUND_NOISE] = floats[1]
        map[OUTPUT_3_CRYING_BABY] = floats[2]
        map[OUTPUT_4_NOISE] = floats[3]
        Timber.i("data: $map")
        return map
    }

    companion object {
        internal const val DATA_SIZE = 441_000
        private const val INPUT_DATA_NAME = "decoded_sample_data:0"
        private const val SAMPLE_RATE_NAME = "decoded_sample_data:1"
        private const val OUTPUT_SCORES_NAME = "labels_softmax"

        private const val OUTPUTS_NUMBER = 4
        const val OUTPUT_1_SILENCE = "SILENCE"
        const val OUTPUT_2_BACKGROUND_NOISE = "BACKGROUND_NOISE"
        const val OUTPUT_3_CRYING_BABY = "CRYING_BABY"
        const val OUTPUT_4_NOISE = "NOISE"

        private val outputScoresNames = arrayOf(OUTPUT_SCORES_NAME)
    }
}
