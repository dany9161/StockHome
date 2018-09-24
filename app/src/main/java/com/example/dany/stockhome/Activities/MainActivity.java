package com.example.dany.stockhome.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dany.stockhome.Fragments.ver_receitasFragment;
import com.example.dany.stockhome.R;

/**
 * Activity principal, é esta activity a primeira a ser chamada
 * Mostra um fragmento do tipo ver_receitasFragment, que mostra as receitas possiveis de fazer
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    ver_receitasFragment verIngredientes;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mostra o fragmento do tipo ver_receitasFragment
        verIngredientes  = new ver_receitasFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.Main,verIngredientes);
        fragmentTransaction.commit();

        //muda a toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //muda o navigationDrawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_searchable_activity);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sugestao) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        } else if (id == R.id.nav_consultarReceitas) {
            startActivity(new Intent(MainActivity.this, ConsultaReceitasActivity.class));
        } else if (id == R.id.nav_inserirReceitas) {
            startActivity(new Intent(MainActivity.this, AddReceitasActivity.class));
        } else if (id == R.id.nav_consultarProdutos) {
            startActivity(new Intent(MainActivity.this, ConsultarProdutosActivity.class));
        } else if (id == R.id.nav_inserirProdutos) {
            startActivity(new Intent(MainActivity.this, AddProdutosActivity.class));
        }else if (id == R.id.nav_verProdutos){
            startActivity(new Intent(MainActivity.this, VerProdutosActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        ver_receitasFragment ver_ingredientes_edita = new ver_receitasFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Nome", query);
        ver_ingredientes_edita.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.Main, ver_ingredientes_edita);
        fragmentTransaction.commit();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}