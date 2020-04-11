package com.example.project01_backup.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_EditPost extends Fragment {
    private View view;
    private EditText etTitle, etAddress, etDescription;
    private TextView tvUser, tvPubDate;
    private AutoCompleteTextView acPlace;
    private Spinner spnCategory;
    private FloatingActionButton fabAddContent;
    private ImageView imgPost, imgContent;
    private CircleImageView imgAvatarUser;
    private ListView lvContent;
    private FirebaseUser user;
    private DAO_Post dao_post;
    private DAO_Content dao_content;
    private DAO_Places dao_places;
    private Post post, oldPost;
    private List<Content> listContent;
    private List<String> nameList;
    private Adapter_LV_Content adapterContent;
    private String idPost;

    public static final int CHOOSE_IMAGE_POST = 2;
    public static final int CHOOSE_IMAGE_CONTENT = 3;
    private int index = -1;
    private Content content;

    public Fragment_EditPost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        initView();
        return view;
    }

    private void initView() {

        user = FirebaseAuth.getInstance().getCurrentUser();
        dao_post = new DAO_Post(getActivity(), this);
        dao_content = new DAO_Content(getActivity(), this);
        dao_places = new DAO_Places(getActivity(), this);
        post = new Post();
        Bundle bundle = getArguments();
        listContent = new ArrayList<>();

        spnCategory = (Spinner) view.findViewById(R.id.fEditPost_spnCategory);
        acPlace = (AutoCompleteTextView) view.findViewById(R.id.fEditPost_acPlace);
        tvUser = (TextView) view.findViewById(R.id.fEditPost_tvUser);
        tvPubDate = (TextView) view.findViewById(R.id.fEditPost_tvPubDate);
        etDescription = (EditText) view.findViewById(R.id.fEditPost_etDescription);
        etTitle = (EditText) view.findViewById(R.id.fEditPost_etTitle);
        etAddress = (EditText) view.findViewById(R.id.fEditPost_etAddress);
        fabAddContent = (FloatingActionButton) view.findViewById(R.id.fEditPost_fabAddContent);
        imgPost = (ImageView) view.findViewById(R.id.fEditPost_imgPost);
        imgAvatarUser = (CircleImageView) view.findViewById(R.id.fEditPost_imgAvatarUser);
        lvContent = (ListView) view.findViewById(R.id.fEditPost_lvContent);

        adapterContent = new Adapter_LV_Content(getActivity(), listContent);
        lvContent.setAdapter(adapterContent);
        nameList = new ArrayList<>();
        String[] categoryList = {"Restaurants", "Accommodations", "Beautiful Places", "Journey Diary"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, categoryList);
        spnCategory.setAdapter(adapterSpinner);
        acPlace.setThreshold(1);

        dao_places.getData(new FirebaseCallback() {
            @Override
            public void placesList(List<Places> placesList) {
                nameList.clear();
                for (Places places : placesList) {
                    nameList.add(places.getName());
                }
                final ArrayAdapter<String> adapterPlace = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, nameList);
                acPlace.setAdapter(adapterPlace);

            }
        });


        setPubDate(tvPubDate);
        tvUser.setText(user.getEmail());
        Picasso.get().load(user.getPhotoUrl()).into(imgAvatarUser);

        if (bundle != null) {
            oldPost = (Post) bundle.getSerializable("post");
            String category = null;
            String categoryOldPost = oldPost.getCategory();
            if (categoryOldPost.equalsIgnoreCase("restaurants")) {
                category = "Ăn Uống";
            } else if (categoryOldPost.equalsIgnoreCase("accommodations")) {
                category = "Chỗ Ở";
            } else if (categoryOldPost.equalsIgnoreCase("beautiful places")) {
                category = "Check in";
            } else if (categoryOldPost.equalsIgnoreCase("journey diary")) {
                category = "Trải Nghiệm";
            }
            idPost = oldPost.getIdPost();
            int position = adapterSpinner.getPosition(category);
            acPlace.setText(oldPost.getPlace());
            spnCategory.setSelection(position);
            etTitle.setText(oldPost.getTittle());
            etAddress.setText(oldPost.getAddress());
            etDescription.setText(oldPost.getDescription());
            Picasso.get().load(Uri.parse(oldPost.getUrlImage())).into(imgPost);
        }
        dao_content.getDataUser(idPost, new FirebaseCallback() {
            @Override
            public void contentListUser(List<Content> contentList) {
                listContent.clear();
                listContent.addAll(contentList);
                adapterContent.notifyDataSetChanged();
            }
        });
        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), CHOOSE_IMAGE_POST);
            }
        });

        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                dialogLongClick();
            }
        });
        fabAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddContent();
            }
        });
    }

    private void dialogAddContent() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_content);
        content = new Content();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText dEtDescription = (EditText) dialog.findViewById(R.id.dAddContent_etDescriptions);
        imgContent = (ImageView) dialog.findViewById(R.id.dAddContent_imgContent);
        Button btnAdd = (Button) dialog.findViewById(R.id.dAddContent_btnAdd);
        Button btnClear = (Button) dialog.findViewById(R.id.dAddContent_btnClear);
        Button btnCancel = (Button) dialog.findViewById(R.id.dAddContent_btnCancel);

        imgContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), CHOOSE_IMAGE_CONTENT);
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
                dEtDescription.setText("");
                imgContent.setImageResource(R.drawable.add_image);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = dEtDescription.getText().toString();
                if (imgContent.getDrawable() == null) {
                    toast("Please, choose a picture");

                } else if (description.isEmpty()) {
                    toast("Please, add a description");
                } else {
                    content.setDescription(dEtDescription.getText().toString());
                    listContent.add(content);
                    adapterContent.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });


        dialog.show();
    }

    private void dialogUpdateContent() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_content);
        content = new Content();
        final Content update = listContent.get(index);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText dEtDescription = (EditText) dialog.findViewById(R.id.dAddContent_etDescriptions);
        imgContent = (ImageView) dialog.findViewById(R.id.dAddContent_imgContent);
        Button btnAdd = (Button) dialog.findViewById(R.id.dAddContent_btnAdd);
        Button btnClear = (Button) dialog.findViewById(R.id.dAddContent_btnClear);
        Button btnCancel = (Button) dialog.findViewById(R.id.dAddContent_btnCancel);

        dEtDescription.setText(update.getDescription());


        imgContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), CHOOSE_IMAGE_CONTENT);
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
                dEtDescription.setText("");
                imgContent.setImageResource(R.drawable.add_image);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (imgContent.getDrawable() == null) {
                    toast("Please, choose a picture");

                } else if (description.isEmpty()) {
                    toast("Please, add a description");
                } else {
                    content.setDescription(dEtDescription.getText().toString());
                    listContent.add(index, content);
                    listContent.remove(update);
                    adapterContent.notifyDataSetChanged();
                    dialog.dismiss();
                }

            }
        });


        dialog.show();
    }

    private void dialogLongClick() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_longclick);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final Content delete = listContent.get(index);
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
                listContent.remove(delete);
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

    private void uploadData() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        final String categoryNode = spnCategory.getSelectedItem().toString();
        final String placeNode = acPlace.getText().toString();
        post.setIdPost(oldPost.getIdPost());
        post.setAddress(etAddress.getText().toString());
        post.setDescription(etDescription.getText().toString());
        post.setPubDate(tvPubDate.getText().toString());
        post.setEmailUser(tvUser.getText().toString());
        post.setTittle(etTitle.getText().toString());
        post.setLongPubDate(longPubDate());
        post.setUrlAvatarUser(String.valueOf(user.getPhotoUrl()));
        post.setIdUser(user.getUid());
        post.setDisplayName(user.getDisplayName());


        if (etTitle.getText().toString().isEmpty() || etDescription.getText().toString().isEmpty() ||
                etAddress.getText().toString().isEmpty() || (imgPost.getDrawable() == null)) {
            toast("Please, fill up the form");
        } else if (listContent.size() == 0) {
            dialog.setMessage("The article has no detailed description. Still submit?.");

            dialog.setNegativeButton("SUBMIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dao_post.insertAdmin(categoryNode, placeNode, post, imgPost);
                    dao_content.deleteUser(oldPost.getIdPost());
                    dao_post.deleteUser(categoryNode, placeNode, oldPost.getIdPost());
                    currentFragment(categoryNode);
                    toast("Pending moderation!");
                }
            });
            dialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        } else {
            dialog.setTitle("Submit an Article?");

            dialog.setNegativeButton("SUBMIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dao_post.insertAdmin(categoryNode, placeNode, post, imgPost);
                    for (int i = 0; i < listContent.size(); i++) {
                        Content upload = new Content();
                        Uri uri = listContent.get(i).getUriImage();
                        upload.setUrlImage(listContent.get(i).getUrlImage());
                        upload.setDescription(listContent.get(i).getDescription());
                        dao_content.insertAdmin(post.getIdPost(), upload, uri);
                    }
                    currentFragment(categoryNode);
                    dao_content.deleteUser(oldPost.getIdPost());
                    dao_post.deleteUser(categoryNode, placeNode, oldPost.getIdPost());
                    toast("Pending moderation!");
                }
            });
            dialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();
        }


    }

    private void setPubDate(TextView tv) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        tv.setText(format.format(calendar.getTime()));
    }

    private void currentFragment(String current) {
        if (current.equalsIgnoreCase("Restaurants")) {
            replaceFragment(new Fragment_Restaurant());
        } else if (current.equalsIgnoreCase("Accommodations")) {
            replaceFragment(new Fragment_Accommodations());
        } else if (current.equalsIgnoreCase("Beautiful Places")) {
            replaceFragment(new Fragment_BeautifulPlaces());
        } else {
            replaceFragment(new Fragment_JourneyDiary());
        }

    }

    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_FrameLayout, fragment)
                .addToBackStack(null)
                .commit();

    }

    private void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private long longPubDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    private void clearPost() {
        etTitle.setText("");
        etAddress.setText("");
        etDescription.setText("");
        imgPost.setImageResource(R.drawable.add_image);
        listContent.clear();
        acPlace.setText("");
        adapterContent.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_post, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_post_complete:
                uploadData();
                break;
            case R.id.menu_post_clear:
                clearPost();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CHOOSE_IMAGE_POST && data != null) {
            imgPost.setImageURI(data.getData());
        } else if (requestCode == CHOOSE_IMAGE_CONTENT && data != null) {
            imgContent.setImageURI(data.getData());
            content.setUriImage(data.getData());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
