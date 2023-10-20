import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView

class ImageAdapter(private val context: Context, private var images: IntArray) : BaseAdapter() {

    override fun getCount(): Int = images.size

    override fun getItem(position: Int): Any? = images[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView: ImageView = if (convertView == null) {
            // If it's not recycled, initialize some attributes
            ImageView(context).apply {
                layoutParams = AbsListView.LayoutParams(240, 240)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        } else {
            convertView as ImageView
        }

        imageView.setImageResource(images[position])
        return imageView
    }

    fun updateImages(newImages: IntArray) {
        this.images = newImages
        notifyDataSetChanged()  // Notify the adapter that data has changed
    }
}
