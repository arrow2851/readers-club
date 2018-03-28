package com.uttej.oraclereadersclub.Home;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uttej.oraclereadersclub.R;
import com.uttej.oraclereadersclub.Utils.UniversalmageLoader;

import org.w3c.dom.Text;

/**
 * Created by Clean on 28-03-2018.
 */

public class ViewBookActivity extends AppCompatActivity{

    private String bookTitle;
    private String bookImagePath;
    private String bookGenres;

    private TextView tvBookTitle;
    private ImageView ivBookCover;
    private ProgressBar bookCoverLoadingProgress;
    private ImageView ivBackArrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        bookTitle = getIntent().getStringExtra(ViewBookActivity.this.getString(R.string.book_title));
        bookImagePath = getIntent().getStringExtra(ViewBookActivity.this.getString(R.string.book_img_url));
        bookGenres = getIntent().getStringExtra(ViewBookActivity.this.getString(R.string.book_genres));

        tvBookTitle = (TextView) findViewById(R.id.postBookTitle);
        ivBookCover = (ImageView) findViewById(R.id.postBookImage);
        ivBackArrow = (ImageView) findViewById(R.id.postBackArrow);
        bookCoverLoadingProgress = (ProgressBar) findViewById(R.id.postBookImageProgress);

        tvBookTitle.setText(bookTitle);
        UniversalmageLoader.setImage(bookImagePath, ivBookCover, bookCoverLoadingProgress, "");

        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
