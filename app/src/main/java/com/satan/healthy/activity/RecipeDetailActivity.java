package com.satan.healthy.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.satan.healthy.R;
import com.satan.healthy.adapter.RecipeAdapter;
import com.satan.healthy.adapter.RelatedRecipeAdapter;
import com.satan.healthy.entitiy.Recipe;
import com.satan.healthy.presenter.IRecipeDetatilPresenter;
import com.satan.healthy.presenter.presenterImpl.RecipeDetailPresenterImpl;
import com.satan.healthy.utils.ConstantFactory;
import com.satan.healthy.utils.ToastUtil;
import com.satan.healthy.view.IRecipeDeatailView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.amap.api.col.c.m;
import static com.satan.healthy.utils.ConstantFactory.DATA_RECIPE;

@ContentView(R.layout.activity_recipe_detail)
public class RecipeDetailActivity extends AppCompatActivity implements IRecipeDeatailView{
    @ViewInject(R.id.toolbar_layout_recipe_detail)
    private CollapsingToolbarLayout mToolbarLayout;
    @ViewInject(R.id.toolbar_recipe_detail)
    private Toolbar mToolbar;
    @ViewInject(R.id.fab_mark_recipe)
    private FloatingActionButton mFab;
    @ViewInject(R.id.tv_recipe_detail_keywords)
    private TextView mTVKeywords;
    @ViewInject(R.id.tv_recipe_detail_food)
    private TextView mTVFood;
    @ViewInject(R.id.tv_recipe_detail_method)
    private TextView mTVMethod;
    @ViewInject(R.id.rv_related_recipes)
    private RecyclerView mRecyclerView;

    private Recipe mRecipe;
    private List<Recipe> mRelatedRecipes;
    private RelatedRecipeAdapter mRecipeAdapter;
    private GridLayoutManager mGridLayoutManager;

    private ToastUtil mToastUtil;
    private IRecipeDetatilPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏透明
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        x.view().inject(this);
        mToastUtil = new ToastUtil(this);
        mPresenter = new RecipeDetailPresenterImpl(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2016/08/23 收藏和取消收藏
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initList();
        showMessage();
    }

    private void showMessage() {
        Intent intent = getIntent();
        mRecipe = (Recipe) intent.getSerializableExtra(DATA_RECIPE);
        mToolbarLayout.setTitle(mRecipe.getName());
        mTVKeywords.setText(mRecipe.getKeywords());
        mTVFood.setText(mRecipe.getFood());
        if (mRecipe.getMessage() == null) {
            mPresenter.getRecipeMethod(mRecipe.getId());
        } else {
            showRecipeMethod();
        }
        //图片
        mPresenter.getImg(mRecipe.getImg());
        //相关菜谱
        mPresenter.getRelatedRecipes(mRecipe.getId(),mRecipe.getName());
    }

    private void showRecipeMethod() {
        Spanned spanned = Html.fromHtml(mRecipe.getMessage());
        mTVMethod.setText(spanned);
    }

    private void initList() {
        mRelatedRecipes = new ArrayList<>();
        mRecipeAdapter = new RelatedRecipeAdapter(R.layout.item_related_recipe, mRelatedRecipes);
        //设置列表项点击监听
        mRecipeAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                finish();
                Intent intent = new Intent(RecipeDetailActivity.this, RecipeDetailActivity.class);
                intent.putExtra(DATA_RECIPE, mRelatedRecipes.get(i));
                startActivity(intent);
            }
        });
        //设置列表项长按监听，拦截长按事件，防止被点击监听处理
        mRecipeAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int i) {
                return true;
            }
        });
        //列表布局管理器
        mGridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        //绑定适配器
        mRecyclerView.setAdapter(mRecipeAdapter);
    }

    @Override
    public void showImg(Bitmap bitmap) {
        mToolbarLayout.setBackground(new BitmapDrawable(bitmap));
    }

    @Override
    public void setRelatedRecipes(List<Recipe> recipes) {
        mRelatedRecipes.addAll(recipes);
        mRecipeAdapter.notifyDataSetChanged();
    }

    @Override
    public void setRecipeMethod(Recipe recipe) {
        mRecipe.setMessage(recipe.getMessage());
        showRecipeMethod();
    }

    @Override
    public void showError(String error) {
        mToastUtil.showToast(error);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }else if(id==R.id.action_share_recipe){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
            Spanned spanned = Html.fromHtml("我在海思健康App查看了<a href=http://www.baidu.com>"+mRecipe.getName()+"</a>的做法，一起来学习养生菜谱吧!");
            intent.putExtra(Intent.EXTRA_TEXT,spanned+"");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, mToolbarLayout.getTitle()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_detail, menu);
        return true;
    }
}
