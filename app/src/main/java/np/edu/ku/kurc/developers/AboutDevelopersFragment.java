package np.edu.ku.kurc.developers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import np.edu.ku.kurc.R;

public class AboutDevelopersFragment extends Fragment {

    private TextView name;
    private TextView email;
    private ImageView avatar;
    private Button visitSiteBtn;

    private int avatarSize;

    private Transformation cropCircleTransformation;

    private Developer amrit;

    public AboutDevelopersFragment() {}

    /**
     * Creates new About Developers Dialog instance.
     *
     * @return New About Developers Dialog instance.
     */
    public static AboutDevelopersFragment instance() {
        return new AboutDevelopersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_developers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        name = (TextView) view.findViewById(R.id.developer_name);
        email = (TextView) view.findViewById(R.id.developer_email);
        avatar = (ImageView) view.findViewById(R.id.developer_avatar);
        visitSiteBtn = (Button) view.findViewById(R.id.developer_visit_site);

        avatarSize = (int) getResources().getDimension(R.dimen.avatar_size_huge);

        cropCircleTransformation = new CropCircleTransformation();

        visitSiteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(amrit.website));
                startActivity(intent);
            }
        });

        loadDevelopers();
    }

    private void loadDevelopers() {
        amrit = new Developer("Amrit Twanabasu",
                                        R.drawable.avatar_amrit,
                                        "amrit.cas@gmail.com",
                                        "http://amrittwanabasu.com.np");

        name.setText(amrit.name);
        email.setText(amrit.email);

        Picasso.with(getContext())
                .load(amrit.avatar)
                .resize(avatarSize,avatarSize)
                .centerCrop()
                .transform(cropCircleTransformation)
                .into(avatar);
    }
}
