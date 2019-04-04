package com.example.alexander.sportdiary;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.alexander.sportdiary.Entities.EditEntities.Aim;
import com.example.alexander.sportdiary.Entities.EditEntities.Block;
import com.example.alexander.sportdiary.Entities.EditEntities.Borg;
import com.example.alexander.sportdiary.Entities.EditEntities.Camp;
import com.example.alexander.sportdiary.Entities.EditEntities.Competition;
import com.example.alexander.sportdiary.Entities.EditEntities.Equipment;
import com.example.alexander.sportdiary.Entities.EditEntities.Exercise;
import com.example.alexander.sportdiary.Entities.EditEntities.Importance;
import com.example.alexander.sportdiary.Entities.EditEntities.Stage;
import com.example.alexander.sportdiary.Entities.EditEntities.Style;
import com.example.alexander.sportdiary.Entities.EditEntities.Tempo;
import com.example.alexander.sportdiary.Entities.EditEntities.Time;
import com.example.alexander.sportdiary.Entities.EditEntities.TrainingPlace;
import com.example.alexander.sportdiary.Entities.EditEntities.Type;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.Entities.EditEntities.Zone;
import com.example.alexander.sportdiary.Fragments.AddNewDiaryFragment;
import com.example.alexander.sportdiary.Fragments.DayFragment;
import com.example.alexander.sportdiary.Fragments.EditFragment;
import com.example.alexander.sportdiary.Fragments.UpdateDiaryFragment;
import com.example.alexander.sportdiary.Utils.NavigationItemLongPressInterceptor;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationItemLongPressInterceptor.OnNavigationItemLongClickListener {

    private static MainActivity instance;
    private SportDataBase database;
    private Toolbar toolbar;
    private DayFragment dayFragment;
    private final static int CALENDAR = 50;
    private Long seasonPlanId;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        database = Room.databaseBuilder(this, SportDataBase.class, "SportDataBase")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<SeasonPlan> diaries = database.seasonPlanDao().getAll();
                MenuItem diaryItem = navigationView.getMenu().findItem(R.id.diaries);
                Menu menu = diaryItem.getSubMenu();
                for(SeasonPlan seasonPlan : diaries) {
                    MenuItem item = menu.add(R.id.diaryMenu,(int)seasonPlan.getId(), 1, seasonPlan.getName()
                            + " " + sdf.format(seasonPlan.getStart()));
                    item.setIcon(R.drawable.ic_menu_share);
                    item.setActionView(new NavigationItemLongPressInterceptor(MainActivity.getInstance()));
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == CALENDAR) {
            dayFragment.changeCalendarVisibility();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_ex) {
            EditFragment<Exercise> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Exercise.class, getString(R.string.Exercises),
                    database.exerciseDao(), getString(R.string.addExercise), getString(R.string.updateExercise));
            dialogFragment.show(getSupportFragmentManager(), "ExerciseDialog");
        } else if (id == R.id.nav_zone) {
            EditFragment<Zone> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Zone.class, getString(R.string.Zones),
                    database.zoneDao(), getString(R.string.addZone), getString(R.string.updateZone));
            dialogFragment.show(getSupportFragmentManager(), "ZoneDialog");
        } else if (id == R.id.nav_aim) {
            EditFragment<Aim> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Aim.class, getString(R.string.aims),
                    database.aimDao(), getString(R.string.addAim), getString(R.string.updateAim));
            dialogFragment.show(getSupportFragmentManager(), "AimDialog");
        } else if (id == R.id.nav_equipment) {
            EditFragment<Equipment> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Equipment.class, getString(R.string.equipment),
                    database.equipmentDao(), getString(R.string.addEquipment), getString(R.string.updateEquipment));
            dialogFragment.show(getSupportFragmentManager(), "EquipmentDialog");
        } else if (id == R.id.nav_time) {
            EditFragment<Time> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Time.class, getString(R.string.times),
                    database.timeDao(), getString(R.string.addTime), getString(R.string.updateTime));
            dialogFragment.show(getSupportFragmentManager(), "TimeDialog");
        } else if (id == R.id.nav_borg) {
            EditFragment<Borg> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Borg.class, getString(R.string.borg_ratings),
                    database.borgDao(), getString(R.string.addBorgRating), getString(R.string.updateBorgRating));
            dialogFragment.show(getSupportFragmentManager(), "BorgRatingDialog");
        } else if (id == R.id.nav_training_place) {
            EditFragment<TrainingPlace> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(TrainingPlace.class, getString(R.string.training_places),
                    database.trainingPlaceDao(), getString(R.string.addTrainingPlace), getString(R.string.updateTrainingPlace));
            dialogFragment.show(getSupportFragmentManager(), "TrainingPlaceDialog");
        } else if (id == R.id.nav_style) {
            EditFragment<Style> styleEditFragment = new EditFragment<>();
            styleEditFragment.setClass(Style.class, getString(R.string.styles),
                    database.styleDao(), getString(R.string.addStyle), getString(R.string.updateStyle));
            styleEditFragment.show(getSupportFragmentManager(), "StyleDialog");
        } else if (id == R.id.nav_tempo) {
            EditFragment<Tempo> tempoEditFragment = new EditFragment<>();
            tempoEditFragment.setClass(Tempo.class, getString(R.string.tempos),
                    database.tempoDao(), getString(R.string.addTempo), getString(R.string.updateTempo));
            tempoEditFragment.show(getSupportFragmentManager(), "tempoDialog");
        } else if (id == R.id.nav_competition) {
            EditFragment<Competition> competitionEditFragment = new EditFragment<>();
            competitionEditFragment.setClass(Competition.class, getString(R.string.competitions),
                    database.competitionDao(), getString(R.string.addCompetition), getString(R.string.updateCompetition));
            competitionEditFragment.show(getSupportFragmentManager(), "competitionDialog");
        } else if (id == R.id.nav_importance) {
            EditFragment<Importance> importanceEditFragment = new EditFragment<>();
            importanceEditFragment.setClass(Importance.class, getString(R.string.importance),
                    database.importanceDao(), getString(R.string.addImportance), getString(R.string.updateImportance));
            importanceEditFragment.show(getSupportFragmentManager(), "importanceDialog");
        } else if (id == R.id.nav_block) {
            EditFragment<Block> blockEditFragment = new EditFragment<>();
            blockEditFragment.setClass(Block.class, getString(R.string.blocks),
                    database.blockDao(), getString(R.string.addBlock), getString(R.string.updateBlock));
            blockEditFragment.show(getSupportFragmentManager(), "blockDialog");
        } else if (id == R.id.nav_stage) {
            EditFragment<Stage> stageEditFragment = new EditFragment<>();
            stageEditFragment.setClass(Stage.class, getString(R.string.stages),
                    database.stageDao(), getString(R.string.addStage), getString(R.string.updateStage));
            stageEditFragment.show(getSupportFragmentManager(), "stageDialog");
        } else if (id == R.id.nav_type) {
            EditFragment<Type> typeEditFragment = new EditFragment<>();
            typeEditFragment.setClass(Type.class, getString(R.string.types),
                    database.typeDao(), getString(R.string.addType), getString(R.string.updateType));
            typeEditFragment.show(getSupportFragmentManager(), "typeDialog");
        } else if (id == R.id.nav_camps) {
            EditFragment<Camp> campEditFragment = new EditFragment<>();
            campEditFragment.setClass(Camp.class, getString(R.string.camps),
                    database.campDao(), getString(R.string.addCamp), getString(R.string.updateCamp));
            campEditFragment.show(getSupportFragmentManager(), "campDialog");
        } else if (id == R.id.nav_plan) {
            if (seasonPlanId == null) {
                Toast.makeText(this, R.string.no_diary_selected, Toast.LENGTH_SHORT).show();
            } else {

            }
        } else if (id == R.id.nav_competition_schedule) {
            if (seasonPlanId == null) {
                Toast.makeText(this, R.string.no_diary_selected, Toast.LENGTH_SHORT).show();
            } else {

            }
        } else if (id == R.id.nav_statistics) {
            if (seasonPlanId == null) {
                Toast.makeText(this, R.string.no_diary_selected, Toast.LENGTH_SHORT).show();
            } else {

            }
        } else if (id == R.id.nav_add) {
            AddNewDiaryFragment diaryFragment = new AddNewDiaryFragment();
            diaryFragment.show(getSupportFragmentManager(), "diaryDialog");
        } else {
            // Handle diary select
            dayFragment = new DayFragment();
            dayFragment.setSeasonPlanId(id);
            seasonPlanId = (long) id;
            toolbar.getMenu().removeItem(CALENDAR);
            toolbar.getMenu().add(0, CALENDAR, 50, "").setIcon(R.drawable.ic_menu_gallery).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            this.setTitle("(" + item.getTitle().charAt(0) + ")");
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, dayFragment)
                    .commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public SportDataBase getDatabase() {
        return database;
    }

    @Override
    public void onNavigationItemLongClick(NavigationItemLongPressInterceptor.SelectedItem itemHandle, View view) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        int id = itemHandle.getItemId(navigationView);
        if(id != R.id.nav_ex && id != R.id.nav_zone && id != R.id.nav_time && id != R.id.nav_aim
                && id != R.id.nav_equipment && id != R.id.nav_borg && id != R.id.nav_tempo
                && id != R.id.nav_training_place && id != R.id.nav_style && id != R.id.nav_competition
                && id != R.id.nav_importance && id != R.id.nav_block && id != R.id.nav_stage
                && id != R.id.nav_type && id != R.id.nav_camps && id != R.id.nav_statistics
                && id != R.id.nav_competition_schedule && id != R.id.nav_plan) {
            UpdateDiaryFragment updateDiaryFragment = new UpdateDiaryFragment();
            updateDiaryFragment.setItemId(id);
            updateDiaryFragment.show(getSupportFragmentManager(), "updateDiary");
        }
    }
}
