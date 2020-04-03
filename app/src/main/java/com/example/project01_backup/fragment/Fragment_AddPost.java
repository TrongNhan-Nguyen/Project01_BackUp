package com.example.project01_backup.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.project01_backup.R;
import com.example.project01_backup.adapter.Adapter_LV_Content;
import com.example.project01_backup.dao.DAO_Content;
import com.example.project01_backup.dao.DAO_Places;
import com.example.project01_backup.dao.DAO_Post;
import com.example.project01_backup.model.Content;
import com.example.project01_backup.model.FirebaseCallback;
import com.example.project01_backup.model.Places;
import com.example.project01_backup.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_AddPost extends Fragment {
    private View view;
    private TextView tvPlace, tvCategory, tvTitle, tvUser, tvPubDate, tvAddress, tvDescription;
    private FloatingActionButton fabAddPost, fabAddContent;
    private ImageView imgPost, imgAvatarUser,dImagePost, dImgAvatar, imgContent;
    private ListView lvContent;
    private FirebaseUser user;
    private DAO_Post dao_post;
    private DAO_Content dao_content;
    private DAO_Places dao_places;
    private Post post;
    private RelativeLayout container;
    private List<Content> contentList;
    private List<String> nameList;
    private Adapter_LV_Content adapterContent;
    public static final int CHOOSE_IMAGE_POST = 2;
    public static final int CHOOSE_IMAGE_CONTENT = 3;
    private int index = -1;
    private Content content;

    public Fragment_AddPost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_post, container, false);
        initView();
        return view;
    }

    private void initView() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        dao_post = new DAO_Post(getActivity(), this);
        dao_content = new DAO_Content(getActivity(),this);
        dao_places = new DAO_Places(getActivity(),this);
        post = new Post();
        contentList = new ArrayList<>();
        container = (RelativeLayout) view.findViewById(R.id.fAddPost_layoutContainer);
        tvDescription = (TextView) view.findViewById(R.id.fAddPost_tvDescription);
        tvCategory = (TextView) view.findViewById(R.id.fAddPost_tvCategory);
        tvPlace = (TextView) view.findViewById(R.id.fAddPost_tvPlace);
        tvPlace = (TextView) view.findViewById(R.id.fAddPost_tvPlace);
        tvUser = (TextView) view.findViewById(R.id.fAddPost_tvUser);
        tvPubDate = (TextView) view.findViewById(R.id.fAddPost_tvPubDate);
        tvTitle = (TextView) view.findViewById(R.id.fAddPost_tvTitle);
        tvAddress = (TextView) view.findViewById(R.id.fAddPost_tvAddress);
        tvDescription = (TextView) view.findViewById(R.id.fAddPost_tvDescription);

        fabAddPost = (FloatingActionButton) view.findViewById(R.id.fAddPost_fabAddPost);
        fabAddContent = (FloatingActionButton) view.findViewById(R.id.fAddPost_fabAddContent);
        imgPost = (ImageView) view.findViewById(R.id.fAddPost_imgPost);
        imgAvatarUser = (ImageView) view.findViewById(R.id.fAddPost_imgAvatarUser);
        lvContent = (ListView) view.findViewById(R.id.fAddPost_lvContent);
        contentList = new ArrayList<>();
        fabAddContent.setVisibility(View.GONE);
        setPubDate(tvPubDate);
        tvUser.setText(user.getEmail());
        Picasso.get().load(user.getPhotoUrl()).into(imgAvatarUser);
        container.setVisibility(View.GONE);
        container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialogAddPost();
                return true;
            }
        });

        lvContent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                dialogLongClick();
                return true;
            }
        });

        fabAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddPost();
            }
        });

        fabAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddContent();
            }
        });



    }

    private void dialogAddPost(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_post);
        dialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        final TextView textViewUser = (TextView) dialog.findViewById(R.id.dAddPost_tvUser);
        final TextView textViewDate = (TextView) dialog.findViewById(R.id.dAddPost_tvPubDate);
        final EditText etTitle = (EditText) dialog.findViewById(R.id.dAddPost_etTitle);
        final EditText etDescription = (EditText) dialog.findViewById(R.id.dAddPost_etDescription);
        final EditText etAddress = (EditText) dialog.findViewById(R.id.dAddPost_etAddress);
        final AutoCompleteTextView acPlace = (AutoCompleteTextView) dialog.findViewById(R.id.dAddPost_acPlaces);
        final Spinner spnCategory = (Spinner) dialog.findViewById(R.id.dAddPost_spnCategory);
        dImagePost  = (ImageView) dialog.findViewById(R.id.dAddPost_imgPost);
        ImageView imgAdd = (ImageView) dialog.findViewById(R.id.dAddPost_imgAddImage);
        dImgAvatar = (ImageView)  (ImageView) dialog.findViewById(R.id.dAddPost_imgAvatar);
        Button btnPost = (Button) dialog.findViewById(R.id.dAddPost_btnPost);
        Button btnClear = (Button) dialog.findViewById(R.id.dAddPost_btnClear);
        Button btnCancel = (Button) dialog.findViewById(R.id.dAddPost_btnCancel);

        nameList = new ArrayList<>();
        String[] categoryList = {"Ăn Uống", "Chỗ Ở", "Check in", " Trải Nghiệm"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line, categoryList);
        final ArrayAdapter<String> adapterPlace = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, nameList);
        acPlace.setAdapter(adapterPlace);
        spnCategory.setAdapter(adapterSpinner);
        acPlace.setThreshold(1);

        dao_places.getData(new FirebaseCallback(){
            @Override
            public void placesList(List<Places> placesList) {
                nameList.clear();
                for (Places places : placesList){
                    nameList.add(places.getName());

                }
                adapterPlace.notifyDataSetChanged();

            }
        });

        setPubDate(textViewDate);
        textViewUser.setText(user.getEmail());
        Picasso.get().load(user.getPhotoUrl()).into(dImgAvatar);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etDescription.setText("");
                etTitle.setText("");
                etAddress.setText("");
            }
        });

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select picture"), CHOOSE_IMAGE_POST);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString();
                String address = etAddress.getText().toString();
                String description = etDescription.getText().toString();
                String mailUser = textViewUser.getText().toString();
                String date = textViewDate.getText().toString();
                String place = acPlace.getText().toString();
                String category = spnCategory.getSelectedItem().toString();
                if (title.isEmpty() || address.isEmpty() || description.isEmpty() || place.isEmpty()){
                    toast("Vui lòng điền đầy đủ thông tin");
                }else if (dImagePost.getDrawable() == null){
                    toast("Vui lòng chọn hình ảnh");

                }else {
                    tvTitle.setText(title);
                    tvAddress.setText(address);
                    tvDescription.setText(description);
                    tvPlace.setText(place);
                    tvCategory.setText(category);
                    container.setVisibility(View.VISIBLE);
                    fabAddContent.setVisibility(View.VISIBLE);
                    fabAddPost.setVisibility(View.GONE);
                    post.setAddress(address);
                    post.setDescription(description);
                    post.setPubDate(date);
                    post.setUser(mailUser);
                    post.setTittle(title);
                    post.setLongPubDate(timeStamp());
                    post.setUrlAvatarUser(String.valueOf(user.getPhotoUrl()));
                    dialog.dismiss();
                }
            }
        });

        dialog.show();

    }

    private void dialogAddContent(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_content);
        content = new Content();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText etDescription = (EditText) dialog.findViewById(R.id.dAddContent_etDescriptions);
        imgContent = (ImageView) dialog.findViewById(R.id.dAddContent_imgContent);
        final ImageView imgChoose = (ImageView) dialog.findViewById(R.id.dAddContent_imgChooseImg);
        Button btnAdd = (Button) dialog.findViewById(R.id.dAddContent_btnAdd);
        Button btnClear = (Button) dialog.findViewById(R.id.dAddContent_btnClear);
        Button btnCancel = (Button) dialog.findViewById(R.id.dAddContent_btnCancel);

        imgChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select picture"), CHOOSE_IMAGE_CONTENT);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("clear");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (imgContent.getDrawable() == null){
                    toast("Vui lòng chọn hình ảnh");

                }else if (description.isEmpty()){
                    toast("Vui lòng thêm mô tả");
                }else {
                    content.setDescription(etDescription.getText().toString());
                    contentList.add(content);
                    adapterContent = new Adapter_LV_Content(getActivity(),contentList);
                    lvContent.setAdapter(adapterContent);
                    dialog.dismiss();
                }

            }
        });


        dialog.show();
    }

    private void dialogUpdateContent(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_content);
        final Content update = contentList.get(index);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText etDescription = (EditText) dialog.findViewById(R.id.dAddContent_etDescriptions);
        imgContent = (ImageView) dialog.findViewById(R.id.dAddContent_imgContent);
        final ImageView imgChoose = (ImageView) dialog.findViewById(R.id.dAddContent_imgChooseImg);
        Button btnAdd = (Button) dialog.findViewById(R.id.dAddContent_btnAdd);
        Button btnClear = (Button) dialog.findViewById(R.id.dAddContent_btnClear);
        Button btnCancel = (Button) dialog.findViewById(R.id.dAddContent_btnCancel);

        imgContent.setImageURI(update.getUriImage());
        etDescription.setText(update.getDescription());

        imgChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select picture"), CHOOSE_IMAGE_CONTENT);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("clear");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (imgContent.getDrawable() == null){
                    toast("Vui lòng chọn hình ảnh");

                }else if (description.isEmpty()){
                    toast("Vui lòng thêm mô tả");
                }else {
//                    content.setDescription(etDescription.getText().toString());
//                    contentList.add(content);
                    update.setDescription(etDescription.getText().toString());
                    adapterContent = new Adapter_LV_Content(getActivity(),contentList);
                    lvContent.setAdapter(adapterContent);
                    dialog.dismiss();
                }

            }
        });


        dialog.show();
    }

    private void dialogLongClick(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_longclick);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Content delete = contentList.get(index);
        Button btnEdit = (Button) dialog.findViewById(R.id.dLongClick_btnEdit);
        Button btnDelete = (Button) dialog.findViewById(R.id.dLongClick_btnDelete);
        Button btnCancel = (Button) dialog.findViewById(R.id.dLongClick_btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentList.remove(delete);
                adapterContent.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUpdateContent();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void uploadData(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        final String pointToNode;
        final String categoryNode = tvCategory.getText().toString();
        final String placeNode = tvPlace.getText().toString();

        if (categoryNode.equalsIgnoreCase("Ăn Uống")){
            pointToNode = "restaurants";
        }else if (categoryNode.equalsIgnoreCase("Chỗ Ở")){
            pointToNode = "accommodations";
        }else if (categoryNode.equalsIgnoreCase("Check in")){
            pointToNode = "beautiful places";
        }else {
            pointToNode = "journey diary";
        }

        if (tvTitle.getText().toString().isEmpty()){
            toast("Vui lòng tạo bài viết");
        }else if (contentList.size() == 0){
            dialog.setMessage("Bài viết hiện tại chưa có thông tin mô tả chi tiết," +
                    " bạn thể thêm thông tin hoặc bỏ qua bước này để tiếp tục đăng" +
                    "bài viết.");

            dialog.setNegativeButton("ĐĂNG BÀI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dao_post.insertAdmin(pointToNode,placeNode,post, imgPost);
                    currentFragment(categoryNode);
                }
            });
            dialog.setPositiveButton("HUỶ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }else {
            dialog.setMessage("Vui lòng kiểm tra lại toàn bộ thông tin trước khi đăng" +
                    " bài viết");

            dialog.setNegativeButton("ĐĂNG BÀI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dao_post.insertAdmin(pointToNode,placeNode,post, imgPost);
                    for (int i = 0; i< contentList.size(); i++){
                        Content upload = new Content();
                        Uri uri = contentList.get(i).getUriImage();
                        upload.setDescription(contentList.get(i).getDescription());
                        dao_content.insertAdmin(post.getId(),upload,uri);
                        currentFragment(categoryNode);
                    }
                }
            });
            dialog.setPositiveButton("HUỶ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }



    }
    private void setPubDate(TextView tv){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        tv.setText(format.format(calendar.getTime()));
    }

    private void currentFragment(String current){
        if (current.equalsIgnoreCase("Ăn Uống")){
            replaceFragment(new Fragment_Restaurant());
        }else if (current.equalsIgnoreCase("Chỗ Ở")){
            replaceFragment(new Fragment_Accommodations());
        }else if (current.equalsIgnoreCase("Check in")){
            replaceFragment(new Fragment_BeautifulPlaces());
        }else {
            replaceFragment(new Fragment_Blog());
        }

    }

    private void replaceFragment(Fragment fragment){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_FrameLayout,fragment)
                .commit();

    }

    private void log (String s){
        Log.d("log",s);
    }
    private void toast (String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
    private long timeStamp(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_post,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_post_complete:
                uploadData();
                break;
            case R.id.menu_post_clear:
                toast("clear all");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CHOOSE_IMAGE_POST && data!=null){
            dImagePost.setImageURI(data.getData());
            imgPost.setImageURI(data.getData());
        }else if (requestCode == CHOOSE_IMAGE_CONTENT && data!=null){
            imgContent.setImageURI(data.getData());
            content.setUriImage(data.getData());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
