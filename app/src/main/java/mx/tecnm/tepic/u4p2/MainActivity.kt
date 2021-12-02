package mx.tecnm.tepic.u4p2


import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var sensorManager1: SensorManager
    lateinit var sensorManager2: SensorManager
    var lastUpdate: Long = 0
    var mProximity: Sensor? = null
    var mAceletor: Sensor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager1 = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mAceletor= sensorManager1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorManager2 = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mProximity= sensorManager2.getDefaultSensor(Sensor.TYPE_PROXIMITY)


        var mfff = LL(this)
        setContentView(mfff)

    }

    override fun onPause() {
        super.onPause()
        sensorManager1.unregisterListener(this)
        sensorManager2.unregisterListener(this)
    }
    override fun onResume() {
        super.onResume()

        sensorManager1.registerListener(this,mAceletor,SensorManager.SENSOR_DELAY_NORMAL)
        mProximity?.also { proximity ->
            sensorManager2.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL)
        }

    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            sepa.setText("COORDENADAS: \nX: ${event.values[0]}" +
                    "\nY: ${event.values[1]} \nZ: ${event.values[2]}")
            var datos = Intent(this, LL::class.java)
            datos.putExtra("datox",event.values[0])
            datos.putExtra("datoy",event.values[1])
            datos.putExtra("datoz",event.values[2])
            startActivity(datos)
        }
        if(event.sensor.type == Sensor.TYPE_PROXIMITY){
            sepa2.setText("Cercania: ${event.values[0]}")
            var datos = Intent(this, LL::class.java)
            datos.putExtra("datof",event.values[0])
            startActivity(datos)
        }
    }
    class LL(context: Context): View(context){
        var tamanoA = 270f
        var tamanoB = 450f
        var incrementoX=5
        var trabajando = false;
        var datos1= 0.00
        var datos2=0.00
        var datos3=0.00
        var proxy = 0f
        var DR =0
        var DG = 0
        var DB = 0
        fun onStartTemporaryDetach(intent: Intent) {
            super.onStartTemporaryDetach()
            var ff = intent.extras
            val dato = ff!!.getDouble("datox")
            datos1=dato
            val dato2 = ff!!.getDouble("datoy")
            datos2=dato2
            val dato3 = ff!!.getDouble("datoz")
            datos3=dato3
            var dato4 = ff!!.getFloat("datof")
            proxy=dato4

        }
        val timer = object : CountDownTimer(2000,100){
            override fun onFinish() {
                start()
            }

            override fun onTick(millisUntilFinished: Long) {

                tamanoA+=incrementoX
                if((datos1<0 && datos2<0 && datos3<0) ||(datos1>0 && datos2>0 && datos3>0)){
                    incrementoX*=-1
                }

                if(proxy<=5.0){
                    DR= 239
                    DG = 231
                    DB = 135
                }
                else {
                    DR= 76
                    DG = 72
                    DB = 79
                }
                invalidate()
            }

        }
        override fun onDraw(c: Canvas) {
            super.onDraw(c)
            val p = Paint()
            c.drawARGB(255,DR,DG,DB)
            val amazon = BitmapFactory.decodeResource(resources,R.drawable.sylveon)
            c.drawBitmap(amazon,tamanoA,tamanoB,p)
            //invalidate()
        }
        override fun onTouchEvent(event: MotionEvent): Boolean {
            if(event.action == MotionEvent.ACTION_DOWN){
                //ENTRA SI SE PRESIONO

            }
            if(event.action == MotionEvent.ACTION_MOVE){
                if(trabajando==false) {
                    timer.start()
                    trabajando=true
                }

            }
            if(event.action == MotionEvent.ACTION_UP){
                //SE LIBERÓ
            }
            invalidate()//vuelve a llamar al onDraw y lo repinta
            return true
        }//class fun onTouchEvent
    }
}
