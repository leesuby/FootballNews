package com.example.football.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.*
import android.util.AttributeSet
import android.view.View
import androidx.collection.lruCache
import androidx.core.graphics.withTranslation
import com.example.football.R


class NewsCustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    //For drawing avatar
    private var avatar : Bitmap?  = null
    private var rectAvatar : Rect = Rect()
    private lateinit var avatarRounded: Bitmap

    //For drawing logo
    private var logo : Bitmap? = null
    private var rectLogo : Rect = Rect()

    //For drawing title
    private var title : String = ""
    private var titlePaint: TextPaint = TextPaint()
    private lateinit var titleSpanable: SpannableString

    //For drawing time
    private var time : String = ""
    private var timePaint: TextPaint = TextPaint()

    //For exception image
    private var bitmapNull : Bitmap


    //to check if data is set
    var readyToDraw  = false


    init {

        //init paint for Title
        titlePaint.color=Color.GRAY
        titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        titlePaint.textSize = 42F

        //init paint for Time
        timePaint.color= Color.LTGRAY
        timePaint.textSize= 34F

        //image for null pointer
        bitmapNull=BitmapFactory.decodeResource(this.resources, R.mipmap.img_no_image)

    }

    fun setAvatarBitmap(bitmap: Bitmap){
        avatar = bitmap

        //get rounded bitmap from bitmap
        avatarRounded = roundedBitmap(bitmap,18F)
    }


    fun setTitle(text: String){
        title = text
        titleSpanable = SpannableString(text)
    }

    fun setLogoBitmap(bitmap: Bitmap){
        logo= bitmap
    }

    fun setTime(text: String){
        time = text
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //if data is ready
        if(readyToDraw) {

            canvas.drawColor(Color.WHITE)

            val width = width
            val height = height

            drawAvatar(canvas,width,height)

            drawLogo(canvas,width,height)

            drawTitle(canvas, width)

            drawTime(canvas,width,height)
        }
    }

    private fun drawLogo(canvas: Canvas,width: Int,height : Int) {
        rectLogo.set((2*width)/5, (4*height)/5, width/2- width/2/18 , height)

        if(logo!= null)
            canvas.drawBitmap(logo!!, null, rectLogo, null)
        else{
            canvas.drawBitmap(bitmapNull, null, rectLogo, null)
        }
    }

    private fun drawAvatar(canvas: Canvas,width: Int,height : Int) {
        rectAvatar.set(width/20, 0, width/3, height)

        if(avatar!=null) {
            canvas.drawBitmap(avatarRounded,null,rectAvatar,null)
        }
        else{
            canvas.drawBitmap(bitmapNull,null,rectAvatar,null)
        }

    }

    private fun drawTitle(canvas: Canvas, width: Int){
        canvas.drawMultilineText(title,titlePaint,width,2/5F* width,0F)

    }

    private fun drawTime(canvas: Canvas,width: Int,height : Int){
        canvas.drawText(time,2.5F*width/5,height.toFloat()-height/16,timePaint)
    }


    //for storing layout on cache to not call many time on onDraw
    private object StaticLayoutCache {

        private const val MAX_SIZE = 50 // Max number of cached items
        private val cache = lruCache<String, StaticLayout>(MAX_SIZE)

        operator fun set(key: String, staticLayout: StaticLayout) {
            cache.put(key, staticLayout)
        }

        operator fun get(key: String): StaticLayout? {
            return cache[key]
        }
    }


    private fun Canvas.drawMultilineText(
        text: CharSequence,
        textPaint: TextPaint,
        width: Int,
        x: Float,
        y: Float,
        start: Int = 0,
        end: Int = text.length,
        alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
        spacingMult: Float = 1f,
        spacingAdd: Float = 0f,
        includePad: Boolean = true,
        ellipsize: TextUtils.TruncateAt? = null,
        maxLines: Int = 2) {

        //for store on cache to call 1 times
        val cacheKey = "$text-$start-$end-$textPaint-$width-$alignment-" +
                "$spacingMult-$spacingAdd-$includePad-$ellipsize"

        val textWidth = width - (2/5F * width)


        // The public constructor was deprecated in API level 28,
        // but the builder is only available from API level 23 onwards
        val staticLayout = StaticLayoutCache[cacheKey] ?:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder
                .obtain(titleSpanable,0,title.length,titlePaint, textWidth.toInt())
                .setAlignment(alignment)
                .setLineSpacing(spacingAdd, spacingMult)
                .setEllipsize(TextUtils.TruncateAt.END)
                .setEllipsizedWidth(textWidth.toInt())
                .setMaxLines(maxLines)
                .build()
                .apply { StaticLayoutCache[cacheKey] = this }
        } else {
            StaticLayout(text, start, end, textPaint, width, alignment,
                spacingMult, spacingAdd, includePad, ellipsize, textWidth.toInt())
                .apply { StaticLayoutCache[cacheKey] = this }
        }

        staticLayout.draw(this, x, y)
    }

    private fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {
        canvas.withTranslation(x, y) {
            draw(this)
        }
    }


    private fun roundedBitmap(bitmap:Bitmap,cornerRadius: Float) : Bitmap{
        // Initialize a new instance of Bitmap
        val dstBitmap = Bitmap.createBitmap(
            bitmap.width,  // Width
            bitmap.height,  // Height
            Bitmap.Config.ARGB_8888 // Config
        )

        // Initialize a new Canvas to draw rounded bitmap
        val canvas = Canvas(dstBitmap)

        // Initialize a new Paint instance
        val paint = Paint()
        paint.isAntiAlias = true


        // Initialize a new Rect instance
        val rect = Rect(0, 0, bitmap.getWidth(), bitmap.getHeight())


        // Initialize a new RectF instance
        val rectF = RectF(rect)


        // Draw a rounded rectangle object on canvas
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)



        // Make a rounded image by copying at the exact center position of source image
        val copyBitmap =bitmap.copy(Bitmap.Config.ARGB_8888,false)
        canvas.drawBitmap(copyBitmap, 0F, 0F, paint)

        // Free the native object associated with this bitmap.
        copyBitmap.recycle()


        // Return the circular bitmap
        return dstBitmap

    }

}