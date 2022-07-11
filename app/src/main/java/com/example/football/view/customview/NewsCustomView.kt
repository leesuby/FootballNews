package com.example.football.view.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.collection.lruCache
import androidx.core.graphics.withTranslation


class NewsCustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    //For drawing avatar
    private var avatar : Bitmap?  = null
    private var rectAvatar : Rect = Rect()

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


    var readyToDraw  = false

    private val DEFAULT_BOARDER_STROKE = 5f
    private var removeBorder = false


    private var borderColor = 0
    private var borderWidth = 0

    init {
        //init paint for Title
        titlePaint.color=Color.GRAY
        titlePaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
        titlePaint.textSize = 42F


        //init paint for Time
        timePaint.color= Color.LTGRAY
        timePaint.textSize= 34F
    }

    fun setAvatarBitmap(bitmap: Bitmap){
        avatar = bitmap
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

        if(readyToDraw) {

            canvas.drawColor(Color.WHITE)

            val width = width
            val height = height


            Log.e("width",width.toString())
            Log.e("height",height.toString())

            drawAvatar(canvas,width,height)

            drawLogo(canvas,width,height)

            drawTitle(canvas,width,height)

            drawTime(canvas,width,height)
        }
    }

    private fun drawLogo(canvas: Canvas,width: Int,height : Int) {
        rectLogo.set((2*width)/5, (4*height)/5, width/2- width/2/18 , height)

        if(logo!= null)
            canvas.drawBitmap(logo!!, null, rectLogo, null)
    }

    private fun drawAvatar(canvas: Canvas,width: Int,height : Int) {
        rectAvatar.set(width/20, 0, width/3, height)

        if(avatar!=null)
            canvas.drawBitmap(avatar!!, null, rectAvatar, null)

    }

    private fun drawTitle(canvas: Canvas,width: Int,height : Int){
        canvas.drawMultilineText(title,titlePaint,width,2/5F* width,0F)

    }

    private fun drawTime(canvas: Canvas,width: Int,height : Int){
        canvas.drawText(time,2.5F*width/5,height.toFloat()-height/16,timePaint)
    }


    //for storing layout on cache
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



    private fun cropBitmap(sourceBmp: Bitmap): Bitmap {
        val outputBmp: Bitmap = if (sourceBmp.width > sourceBmp.height) {
            Bitmap.createBitmap(sourceBmp, 0, 0, sourceBmp.height, sourceBmp.height)
        } else if (sourceBmp.width < sourceBmp.height) {
            Bitmap.createBitmap(sourceBmp, 0, 0, sourceBmp.width, sourceBmp.width)
        } else {
            sourceBmp
        }
        return outputBmp
    }

    private fun getRoundedCroppedBitmap(bitmap: Bitmap, radius: Int): Bitmap {
        val finalBitmap: Bitmap = if (bitmap.width != radius || bitmap.height != radius) {
            // Set the filter to false, because we don't need very smooth one! It's cheaper!
            Bitmap.createScaledBitmap(bitmap, radius, radius, false)
        } else {
            bitmap
        }
        val output =
            Bitmap.createBitmap(finalBitmap.width, finalBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, finalBitmap.width, finalBitmap.height)
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawARGB(0, 0, 0, 0)

        // It doesn't matter which color!
        paint.color = Color.WHITE
        canvas.drawRoundRect(
            RectF(
                0F, 0F, finalBitmap.width.toFloat(),
                finalBitmap.height.toFloat()
            ), 15F, 15F, paint
        )


        // The second drawing should only be visible of if overlapping with the first
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(finalBitmap, rect, rect, paint)

        // Draw the border, if exist
        if (!removeBorder) {
            canvas.drawRoundRect(
                RectF(
                    0F, 0F, finalBitmap.width.toFloat(),
                    finalBitmap.height.toFloat()
                ), 15F, 15F, getBorderPaint()!!
            )
        }
        return output
    }

    private fun getBorderPaint(): Paint? {
        val borderPaint = Paint()
        if (borderColor !== 0) {
            borderPaint.color = borderColor
        } else {
            borderPaint.color = Color.WHITE
        }
        if (borderWidth !== 0) {
            borderPaint.strokeWidth = borderWidth.toFloat()
        } else {
            borderPaint.strokeWidth = DEFAULT_BOARDER_STROKE
        }
        borderPaint.style = Paint.Style.STROKE
        borderPaint.isAntiAlias = true
        return borderPaint
    }




    /**
     * A method to se the color of the border of the image view
     *
     * @param colorResource The resource id of the favourite color
     */
    fun setBorderColor(colorResource: Int) {
        this.borderColor = colorResource
    }

    /**
     * A method to set the thickness of the border of the image view
     *
     * @param width The width of the border stroke in pixels
     */
    fun setBorderWidth(width: Int) {
        this.borderWidth = width
    }

    /**
     * A method to set whether show the image view with border or not
     *
     * @param removeBorder true to remove the border of the iamge view, otherwise it will have a border
     */
    private fun removeBorder(removeBorder: Boolean) {
        this.removeBorder = removeBorder
    }


}