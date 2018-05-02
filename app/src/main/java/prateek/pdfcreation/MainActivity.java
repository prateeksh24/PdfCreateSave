package prateek.pdfcreation;

import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * Created by prateek kumar sharma on 02/5/18.
 */
public class MainActivity extends AppCompatActivity {

    Button btn;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=(Button)findViewById(R.id.btn);
        layout=(LinearLayout)findViewById(R.id.layout);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PdfGenerationTask().execute();
            }
        });
    }

    private class PdfGenerationTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            PdfDocument document = new PdfDocument();

            // repaint the user's text into the page
            View content =(LinearLayout)findViewById(R.id.layout);

            // crate a page description
            int pageNumber = 1;
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(content.getWidth(),
                    content.getHeight() - 20, pageNumber).create();

            // create a new page from the PageInfo
            PdfDocument.Page page = document.startPage(pageInfo);

            content.draw(page.getCanvas());

            // do final processing of the page
            document.finishPage(page);

            SimpleDateFormat sdf = new SimpleDateFormat(" d MMM yyyy hh:mm aaa");
            String pdfName = "PDF " + sdf.format(Calendar.getInstance().getTime()) + ".pdf";


            File outputFile = new File(MainActivity.this.getExternalFilesDir("Prateek PDF Example"), pdfName);

            try {
                outputFile.createNewFile();
                OutputStream out = new FileOutputStream(outputFile);
                document.writeTo(out);
                document.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return outputFile.getPath();
        }

        @Override
        protected void onPostExecute(String filePath) {
            if (filePath != null) {
                Toast.makeText(MainActivity.this, "Pdf saved at " + filePath, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(MainActivity.this, "Error in Pdf creation" + filePath, Toast.LENGTH_SHORT).show();
            }

        }

    }
}
