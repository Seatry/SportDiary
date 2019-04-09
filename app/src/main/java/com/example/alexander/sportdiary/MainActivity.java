package com.example.alexander.sportdiary;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.alexander.sportdiary.Adapters.ExpandableListAdapter;
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
import com.example.alexander.sportdiary.Fragments.CompetitionScheduleFragment;
import com.example.alexander.sportdiary.Fragments.DayFragment;
import com.example.alexander.sportdiary.Fragments.EditFragment;
import com.example.alexander.sportdiary.Fragments.OverallPlanFragment;
import com.example.alexander.sportdiary.Fragments.Statistics;
import com.example.alexander.sportdiary.Fragments.UpdateDiaryFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.alexander.sportdiary.MenuItemIds.*;
import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static MainActivity instance;
    private SportDataBase database;
    private Toolbar toolbar;
    private DayFragment dayFragment;


    public void setDayFragment(DayFragment dayFragment) {
        this.dayFragment = dayFragment;
    }

    @Nullable
    public static Long getSeasonPlanId() {
        return seasonPlanId;
    }

    private static Long seasonPlanId;

    private static ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;

    public static List<MenuModel> getHeaderList() {
        return headerList;
    }

    public static HashMap<MenuModel, List<MenuModel>> getChildList() {
        return childList;
    }

    private static List<MenuModel> headerList = new ArrayList<>();
    private static HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    public static MainActivity getInstance() {
        return instance;
    }

    public static ExpandableListAdapter getExpandableListAdapter() {
        return expandableListAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        expandableListView = findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();

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
                MenuModel menuModel = MenuModel.getMenuModelById(headerList, DIARY_GROUP.getValue());
                List<MenuModel> childs = childList.get(menuModel);
                for(SeasonPlan seasonPlan : diaries) {
                    String diaryName = seasonPlan.getName() + " " + sdf.format(seasonPlan.getStart());
                    MenuModel childModel = new MenuModel(diaryName, false, false, ((int) seasonPlan.getId()));
                    childs.add(0, childModel);
                    childList.put(menuModel, childs);
                }
            }
        });
    }

    private void prepareMenuData() {
        MenuModel menuModel = new MenuModel(getString(R.string.lists_edit), true, true, EDIT_GROUP.getValue());
        headerList.add(menuModel);
        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel(getString(R.string.Exercises), false, false, EXERCISES.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.Zones), false, false, ZONES.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.aims), false, false, AIMS.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.equipment), false, false, EQUIPMENTS.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.times), false, false, TIMES.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.training_places), false, false, TRAINING_PLACES.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.borg_ratings), false, false, BORG_RATINGS.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.styles), false, false, STYLES.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.tempos), false, false, TEMPOS.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.competitions), false, false, COMPETITIONS.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.importance), false, false, IMPORTANCE.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.blocks), false, false, BLOCKS.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.stages), false, false, STAGES.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.types), false, false, TYPES.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.camps), false, false, CAMPS.getValue());
        childModelsList.add(childModel);

        if (menuModel.isHasChildren()) {
            childList.put(menuModel, childModelsList);
        }

        menuModel = new MenuModel(getString(R.string.overall_plan), true, false, OVERALL_PLAN.getValue());
        headerList.add(menuModel);

        if (!menuModel.isHasChildren()) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel(getString(R.string.competition_schedule), true, false, COMPETITION_SCHEDULE.getValue());
        headerList.add(menuModel);

        if (!menuModel.isHasChildren()) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel(getString(R.string.statistics), true, false, STATISTICS.getValue());
        headerList.add(menuModel);

        if (!menuModel.isHasChildren()) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel(getString(R.string.diaries), true, true, DIARY_GROUP.getValue());
        headerList.add(menuModel);
        childModelsList = new ArrayList<>();
        if (menuModel.isHasChildren()) {
            childList.put(menuModel, childModelsList);
        }

        menuModel = new MenuModel(getString(R.string.add), true, false, ADD_DIARY.getValue());
        headerList.add(menuModel);

        if (!menuModel.isHasChildren()) {
            childList.put(menuModel, null);
        }

    }

    private void populateExpandableList() {
        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup()) {
                    if (!headerList.get(groupPosition).isHasChildren()) {
                        handleClick(headerList.get(groupPosition));
                        onBackPressed();
                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    handleClick(model);
                    onBackPressed();
                }

                return false;
            }
        });

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    // You now have everything that you would as if this was an OnChildClickListener()
                    // Add your logic here.

                    if (childList.get(headerList.get(groupPosition)) != null) {
                        int groupId = headerList.get(groupPosition).getId();
                        if (groupId == DIARY_GROUP.getValue()) {
                            handleLongClick(childList.get(headerList.get(groupPosition)).get(childPosition).getId());
                        }
                        onBackPressed();
                        // Return true as we are handling the event.
                        return true;
                    }
                }

                return false;
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
        } else if (id == CALENDAR.getValue()) {
            dayFragment.changeCalendarVisibility();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void handleClick(MenuModel model) {
        // Handle navigation view item clicks here.
        int id = model.getId();
        if (id == EXERCISES.getValue()) {
            EditFragment<Exercise> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Exercise.class, getString(R.string.Exercises),
                    database.exerciseDao(), getString(R.string.addExercise), getString(R.string.updateExercise));
            dialogFragment.show(getSupportFragmentManager(), "ExerciseDialog");
        } else if (id == ZONES.getValue()) {
            EditFragment<Zone> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Zone.class, getString(R.string.Zones),
                    database.zoneDao(), getString(R.string.addZone), getString(R.string.updateZone));
            dialogFragment.show(getSupportFragmentManager(), "ZoneDialog");
        } else if (id == AIMS.getValue()) {
            EditFragment<Aim> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Aim.class, getString(R.string.aims),
                    database.aimDao(), getString(R.string.addAim), getString(R.string.updateAim));
            dialogFragment.show(getSupportFragmentManager(), "AimDialog");
        } else if (id == EQUIPMENTS.getValue()) {
            EditFragment<Equipment> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Equipment.class, getString(R.string.equipment),
                    database.equipmentDao(), getString(R.string.addEquipment), getString(R.string.updateEquipment));
            dialogFragment.show(getSupportFragmentManager(), "EquipmentDialog");
        } else if (id == TIMES.getValue()) {
            EditFragment<Time> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Time.class, getString(R.string.times),
                    database.timeDao(), getString(R.string.addTime), getString(R.string.updateTime));
            dialogFragment.show(getSupportFragmentManager(), "TimeDialog");
        } else if (id == BORG_RATINGS.getValue()) {
            EditFragment<Borg> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Borg.class, getString(R.string.borg_ratings),
                    database.borgDao(), getString(R.string.addBorgRating), getString(R.string.updateBorgRating));
            dialogFragment.show(getSupportFragmentManager(), "BorgRatingDialog");
        } else if (id == TRAINING_PLACES.getValue()) {
            EditFragment<TrainingPlace> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(TrainingPlace.class, getString(R.string.training_places),
                    database.trainingPlaceDao(), getString(R.string.addTrainingPlace), getString(R.string.updateTrainingPlace));
            dialogFragment.show(getSupportFragmentManager(), "TrainingPlaceDialog");
        } else if (id == STYLES.getValue()) {
            EditFragment<Style> styleEditFragment = new EditFragment<>();
            styleEditFragment.setClass(Style.class, getString(R.string.styles),
                    database.styleDao(), getString(R.string.addStyle), getString(R.string.updateStyle));
            styleEditFragment.show(getSupportFragmentManager(), "StyleDialog");
        } else if (id == TEMPOS.getValue()) {
            EditFragment<Tempo> tempoEditFragment = new EditFragment<>();
            tempoEditFragment.setClass(Tempo.class, getString(R.string.tempos),
                    database.tempoDao(), getString(R.string.addTempo), getString(R.string.updateTempo));
            tempoEditFragment.show(getSupportFragmentManager(), "tempoDialog");
        } else if (id == COMPETITIONS.getValue()) {
            EditFragment<Competition> competitionEditFragment = new EditFragment<>();
            competitionEditFragment.setClass(Competition.class, getString(R.string.competitions),
                    database.competitionDao(), getString(R.string.addCompetition), getString(R.string.updateCompetition));
            competitionEditFragment.show(getSupportFragmentManager(), "competitionDialog");
        } else if (id == IMPORTANCE.getValue()) {
            EditFragment<Importance> importanceEditFragment = new EditFragment<>();
            importanceEditFragment.setClass(Importance.class, getString(R.string.importance),
                    database.importanceDao(), getString(R.string.addImportance), getString(R.string.updateImportance));
            importanceEditFragment.show(getSupportFragmentManager(), "importanceDialog");
        } else if (id == BLOCKS.getValue()) {
            EditFragment<Block> blockEditFragment = new EditFragment<>();
            blockEditFragment.setClass(Block.class, getString(R.string.blocks),
                    database.blockDao(), getString(R.string.addBlock), getString(R.string.updateBlock));
            blockEditFragment.show(getSupportFragmentManager(), "blockDialog");
        } else if (id == STAGES.getValue()) {
            EditFragment<Stage> stageEditFragment = new EditFragment<>();
            stageEditFragment.setClass(Stage.class, getString(R.string.stages),
                    database.stageDao(), getString(R.string.addStage), getString(R.string.updateStage));
            stageEditFragment.show(getSupportFragmentManager(), "stageDialog");
        } else if (id == TYPES.getValue()) {
            EditFragment<Type> typeEditFragment = new EditFragment<>();
            typeEditFragment.setClass(Type.class, getString(R.string.types),
                    database.typeDao(), getString(R.string.addType), getString(R.string.updateType));
            typeEditFragment.show(getSupportFragmentManager(), "typeDialog");
        } else if (id == CAMPS.getValue()) {
            EditFragment<Camp> campEditFragment = new EditFragment<>();
            campEditFragment.setClass(Camp.class, getString(R.string.camps),
                    database.campDao(), getString(R.string.addCamp), getString(R.string.updateCamp));
            campEditFragment.show(getSupportFragmentManager(), "campDialog");
        } else if (id == OVERALL_PLAN.getValue()) {
            if (seasonPlanId == null) {
                Toast.makeText(this, R.string.no_diary_selected, Toast.LENGTH_SHORT).show();
            } else {
                OverallPlanFragment overallPlanFragment = new OverallPlanFragment();
                overallPlanFragment.setSeasonPlanId(seasonPlanId);
                overallPlanFragment.show(getSupportFragmentManager(), "planDialog");
            }
        } else if (id == COMPETITION_SCHEDULE.getValue()) {
            if (seasonPlanId == null) {
                Toast.makeText(this, R.string.no_diary_selected, Toast.LENGTH_SHORT).show();
            } else {
                CompetitionScheduleFragment scheduleFragment = new CompetitionScheduleFragment();
                scheduleFragment.setSeasonPlanId(seasonPlanId);
                scheduleFragment.show(getSupportFragmentManager(), "scheduleDialog");
            }
        } else if (id == STATISTICS.getValue()) {
            if (seasonPlanId == null) {
                Toast.makeText(this, R.string.no_diary_selected, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.getInstance(), Statistics.class);
                intent.putExtra("seasonPlanId", seasonPlanId);
                this.startActivity(intent);
            }
        } else if (id == ADD_DIARY.getValue()) {
            AddNewDiaryFragment diaryFragment = new AddNewDiaryFragment();
            diaryFragment.show(getSupportFragmentManager(), "diaryDialog");
        } else {
            // Handle diary select
            dayFragment = new DayFragment();
            dayFragment.setSeasonPlanId(id);
            seasonPlanId = (long) id;
            toolbar.getMenu().removeItem(CALENDAR.getValue());
            toolbar.getMenu().add(0, CALENDAR.getValue(), 50, "").setIcon(R.drawable.ic_menu_gallery).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            this.setTitle("(" + model.getMenuName().charAt(0) + ")");
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_frame, dayFragment)
                    .commit();
        }
    }

    public SportDataBase getDatabase() {
        return database;
    }

    public void handleLongClick(int id) {
        UpdateDiaryFragment updateDiaryFragment = new UpdateDiaryFragment();
        updateDiaryFragment.setItemId(id);
        updateDiaryFragment.show(getSupportFragmentManager(), "updateDiary");
    }

    public static void putToChildList(MenuModel menuModel, List<MenuModel> childs) {
        childList.put(menuModel, childs);
    }
}
