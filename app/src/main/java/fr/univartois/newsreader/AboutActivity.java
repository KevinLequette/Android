package fr.univartois.newsreader;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        TextView descProjet = findViewById(R.id.descProjet);;
        String description = "Ce projet prend place dans le module M4206 lors du semestre 4. Par groupe de trois nous avons dû réaliser une application de lecture de flux RSS permettant notamment l'affichage de titres/résumés avec la possibilité d'ouvrir les articles dans un navigateur. De plus l'application contient deux pages annexes : Settings et About. L'activité Settings permet de changer l'URL de la page principale et de vérifier si cette URL est valide. L'activité About affiche ce texte qui résume notre projet." +
                "\n\nProjet réalisé par : \nKévin LEQUETTE, Frédéric MALFAIT et Teddie SOLER.";
        descProjet.setText(description);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}