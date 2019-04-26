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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.alexander.sportdiary.Adapters.ExpandableListAdapter;
import com.example.alexander.sportdiary.Auth.GoogleSignInActivity;
import com.example.alexander.sportdiary.Converters.EntityDtoConverter;
import com.example.alexander.sportdiary.DataBase.SportDataBase;
import com.example.alexander.sportdiary.Sync.SyncDataBase;
import com.example.alexander.sportdiary.Entities.EditEntities.Aim;
import com.example.alexander.sportdiary.Entities.EditEntities.Block;
import com.example.alexander.sportdiary.Entities.EditEntities.Borg;
import com.example.alexander.sportdiary.Entities.EditEntities.Camp;
import com.example.alexander.sportdiary.Entities.EditEntities.Competition;
import com.example.alexander.sportdiary.Entities.EditEntities.Equipment;
import com.example.alexander.sportdiary.Entities.EditEntities.Exercise;
import com.example.alexander.sportdiary.Entities.EditEntities.Importance;
import com.example.alexander.sportdiary.Entities.EditEntities.RestPlace;
import com.example.alexander.sportdiary.Entities.EditEntities.Stage;
import com.example.alexander.sportdiary.Entities.EditEntities.Style;
import com.example.alexander.sportdiary.Entities.EditEntities.Tempo;
import com.example.alexander.sportdiary.Entities.EditEntities.Test;
import com.example.alexander.sportdiary.Entities.EditEntities.Time;
import com.example.alexander.sportdiary.Entities.EditEntities.TrainingPlace;
import com.example.alexander.sportdiary.Entities.EditEntities.Type;
import com.example.alexander.sportdiary.Entities.EditEntities.Zone;
import com.example.alexander.sportdiary.Entities.SeasonPlan;
import com.example.alexander.sportdiary.Enums.Table;
import com.example.alexander.sportdiary.Fragments.AddNewDiaryFragment;
import com.example.alexander.sportdiary.Fragments.CompetitionScheduleFragment;
import com.example.alexander.sportdiary.Fragments.DayFragment;
import com.example.alexander.sportdiary.Fragments.EditFragment;
import com.example.alexander.sportdiary.Fragments.OverallPlanFragment;
import com.example.alexander.sportdiary.Fragments.Statistics;
import com.example.alexander.sportdiary.Fragments.UpdateDiaryFragment;
import com.example.alexander.sportdiary.Menu.MenuModel;
import com.example.alexander.sportdiary.Sync.SyncWorker;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import static com.example.alexander.sportdiary.Enums.SignType.NEW_SIGN;
import static com.example.alexander.sportdiary.Enums.SignType.OLD_SIGN;
import static com.example.alexander.sportdiary.Enums.SyncOption.DELETE;
import static com.example.alexander.sportdiary.Enums.SyncOption.SAVE;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.ADD_DIARY;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.AIMS;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.BLOCKS;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.BORG_RATINGS;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.CALENDAR;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.CAMPS;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.COMPETITIONS;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.COMPETITION_SCHEDULE;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.DIARY_GROUP;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.EDIT_GROUP;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.EQUIPMENTS;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.EXERCISES;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.IMPORTANCE;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.OVERALL_PLAN;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.REST_PLACE;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.STAGES;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.STATISTICS;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.STYLES;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.TEMPOS;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.TEST;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.TIMES;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.TRAINING_PLACES;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.TYPES;
import static com.example.alexander.sportdiary.Menu.MenuItemIds.ZONES;
import static com.example.alexander.sportdiary.Utils.DateUtil.sdf;
import static com.google.android.gms.common.api.CommonStatusCodes.SUCCESS;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static MainActivity instance;
    private static SportDataBase database;
    private Toolbar toolbar;
    private DayFragment dayFragment;
    private static String userId;
    private static String token;
    private static String signType;
    private static Long seasonPlanId;
    private static ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expandableListView;
    private static List<MenuModel> headerList = new ArrayList<>();
    private static HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final EntityDtoConverter converter = new EntityDtoConverter();

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static EntityDtoConverter getConverter() {
        return converter;
    }

    public void setDayFragment(DayFragment dayFragment) {
        this.dayFragment = dayFragment;
    }

    @Nullable
    public static Long getSeasonPlanId() {
        return seasonPlanId;
    }

    public static String getUserId() {
        return userId;
    }

    public static List<MenuModel> getHeaderList() {
        return headerList;
    }

    public static HashMap<MenuModel, List<MenuModel>> getChildList() {
        return childList;
    }

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

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId" );
        signType = intent.getStringExtra("signType");
        token = intent.getStringExtra("token");

        Log.d("TOKEN", token);

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

        if (signType.equals(OLD_SIGN.toString())) {
            fillDiariesMenu();
        }

        initializeDataBase();
    }

    private void initializeDataBase() {
        if (signType.equals(NEW_SIGN.toString())) {
            signType = OLD_SIGN.toString();
            new SyncDataBase().execute(userId, token);
        }
    }

    public void fillDiariesMenu() {
        AsyncTask.execute(() -> {
            List<SeasonPlan> diaries = database.seasonPlanDao().getAllByUserId(userId);
            MenuModel menuModel = MenuModel.getMenuModelById(headerList, DIARY_GROUP.getValue());
            List<MenuModel> childs = childList.get(menuModel);
            for(SeasonPlan seasonPlan : diaries) {
                String diaryName = seasonPlan.getName() + " " + sdf.format(seasonPlan.getStart());
                MenuModel childModel = new MenuModel(diaryName, false, false, ((int) seasonPlan.getId()));
                childs.add(0, childModel);
                childList.put(menuModel, childs);
            }
        });
    }

    public static void syncSave(String data, String table) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build();
        Data inputData = new Data.Builder()
                .putString("data", data)
                .putString("table", table)
                .putString("option", SAVE.toString())
                .putString("userId", userId)
                .putString("token", token)
                .build();
        OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(SyncWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();
        WorkManager.getInstance().enqueueUniqueWork("sync data", ExistingWorkPolicy.APPEND, syncRequest);
    }

    public static void syncSeasonPlan(Long id, String table) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build();
        Data inputData = new Data.Builder()
                .putLong("id", id)
                .putString("table", table)
                .putString("option", SAVE.toString())
                .putString("userId", userId)
                .putString("token", token)
                .build();
        OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(SyncWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();
        WorkManager.getInstance().enqueueUniqueWork("sync data", ExistingWorkPolicy.APPEND, syncRequest);
    }

    public static void syncDelete(Long id, String table) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED).build();
        Data inputData = new Data.Builder()
                .putLong("id", id)
                .putString("table", table)
                .putString("option", DELETE.toString())
                .putString("userId", userId)
                .putString("token", token)
                .build();
        OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(SyncWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();
        WorkManager.getInstance().enqueueUniqueWork("sync data", ExistingWorkPolicy.APPEND, syncRequest);
    }

    private void prepareMenuData() {
        headerList.clear();
        childList.clear();
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
        childModel = new MenuModel(getString(R.string.rest_places), false, false, REST_PLACE.getValue());
        childModelsList.add(childModel);
        childModel = new MenuModel(getString(R.string.test), false, false, TEST.getValue());
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

        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {

            if (headerList.get(groupPosition).isGroup()) {
                if (!headerList.get(groupPosition).isHasChildren()) {
                    handleClick(headerList.get(groupPosition));
                    onBackPressed();
                }
            }

            return false;
        });

        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            if (childList.get(headerList.get(groupPosition)) != null) {
                MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                handleClick(model);
                onBackPressed();
            }

            return false;
        });

        expandableListView.setOnItemLongClickListener((parent, view, position, id) -> {
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
        if (id == R.id.sign_out) {
            signOut();
        } else if (id == CALENDAR.getValue()) {
            dayFragment.changeCalendarVisibility();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (code != SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, code, 1).show();
        }
    }

    public void signOut() {
        // Firebase sign out
        GoogleSignInActivity.getmAuth().signOut();

        // Google sign out
        GoogleSignInActivity.getmGoogleSignInClient().signOut().addOnCompleteListener(this,
                task -> getInstance().finish());
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
                    database.exerciseDao(), getString(R.string.addExercise), getString(R.string.updateExercise), Table.EXERCISE);
            dialogFragment.show(getSupportFragmentManager(), "ExerciseDialog");
        } else if (id == ZONES.getValue()) {
            EditFragment<Zone> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Zone.class, getString(R.string.Zones),
                    database.zoneDao(), getString(R.string.addZone), getString(R.string.updateZone), Table.ZONE);
            dialogFragment.show(getSupportFragmentManager(), "ZoneDialog");
        } else if (id == AIMS.getValue()) {
            EditFragment<Aim> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Aim.class, getString(R.string.aims),
                    database.aimDao(), getString(R.string.addAim), getString(R.string.updateAim), Table.AIM);
            dialogFragment.show(getSupportFragmentManager(), "AimDialog");
        } else if (id == EQUIPMENTS.getValue()) {
            EditFragment<Equipment> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Equipment.class, getString(R.string.equipment),
                    database.equipmentDao(), getString(R.string.addEquipment), getString(R.string.updateEquipment), Table.EQUIPMENT);
            dialogFragment.show(getSupportFragmentManager(), "EquipmentDialog");
        } else if (id == TIMES.getValue()) {
            EditFragment<Time> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Time.class, getString(R.string.times),
                    database.timeDao(), getString(R.string.addTime), getString(R.string.updateTime), Table.TIME);
            dialogFragment.show(getSupportFragmentManager(), "TimeDialog");
        } else if (id == BORG_RATINGS.getValue()) {
            EditFragment<Borg> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(Borg.class, getString(R.string.borg_ratings),
                    database.borgDao(), getString(R.string.addBorgRating), getString(R.string.updateBorgRating), Table.BORG);
            dialogFragment.show(getSupportFragmentManager(), "BorgRatingDialog");
        } else if (id == TRAINING_PLACES.getValue()) {
            EditFragment<TrainingPlace> dialogFragment = new EditFragment<>();
            dialogFragment.setClass(TrainingPlace.class, getString(R.string.training_places),
                    database.trainingPlaceDao(), getString(R.string.addTrainingPlace), getString(R.string.updateTrainingPlace), Table.TRAINING_PLACE);
            dialogFragment.show(getSupportFragmentManager(), "TrainingPlaceDialog");
        } else if (id == STYLES.getValue()) {
            EditFragment<Style> styleEditFragment = new EditFragment<>();
            styleEditFragment.setClass(Style.class, getString(R.string.styles),
                    database.styleDao(), getString(R.string.addStyle), getString(R.string.updateStyle), Table.STYLE);
            styleEditFragment.show(getSupportFragmentManager(), "StyleDialog");
        } else if (id == TEMPOS.getValue()) {
            EditFragment<Tempo> tempoEditFragment = new EditFragment<>();
            tempoEditFragment.setClass(Tempo.class, getString(R.string.tempos),
                    database.tempoDao(), getString(R.string.addTempo), getString(R.string.updateTempo), Table.TEMPO);
            tempoEditFragment.show(getSupportFragmentManager(), "tempoDialog");
        } else if (id == COMPETITIONS.getValue()) {
            EditFragment<Competition> competitionEditFragment = new EditFragment<>();
            competitionEditFragment.setClass(Competition.class, getString(R.string.competitions),
                    database.competitionDao(), getString(R.string.addCompetition), getString(R.string.updateCompetition), Table.COMPETITION);
            competitionEditFragment.show(getSupportFragmentManager(), "competitionDialog");
        } else if (id == IMPORTANCE.getValue()) {
            EditFragment<Importance> importanceEditFragment = new EditFragment<>();
            importanceEditFragment.setClass(Importance.class, getString(R.string.importance),
                    database.importanceDao(), getString(R.string.addImportance), getString(R.string.updateImportance), Table.IMPORTANCE);
            importanceEditFragment.show(getSupportFragmentManager(), "importanceDialog");
        } else if (id == BLOCKS.getValue()) {
            EditFragment<Block> blockEditFragment = new EditFragment<>();
            blockEditFragment.setClass(Block.class, getString(R.string.blocks),
                    database.blockDao(), getString(R.string.addBlock), getString(R.string.updateBlock), Table.BLOCK);
            blockEditFragment.show(getSupportFragmentManager(), "blockDialog");
        } else if (id == STAGES.getValue()) {
            EditFragment<Stage> stageEditFragment = new EditFragment<>();
            stageEditFragment.setClass(Stage.class, getString(R.string.stages),
                    database.stageDao(), getString(R.string.addStage), getString(R.string.updateStage), Table.STAGE);
            stageEditFragment.show(getSupportFragmentManager(), "stageDialog");
        } else if (id == TYPES.getValue()) {
            EditFragment<Type> typeEditFragment = new EditFragment<>();
            typeEditFragment.setClass(Type.class, getString(R.string.types),
                    database.typeDao(), getString(R.string.addType), getString(R.string.updateType), Table.TYPE);
            typeEditFragment.show(getSupportFragmentManager(), "typeDialog");
        } else if (id == CAMPS.getValue()) {
            EditFragment<Camp> campEditFragment = new EditFragment<>();
            campEditFragment.setClass(Camp.class, getString(R.string.camps),
                    database.campDao(), getString(R.string.addCamp), getString(R.string.updateCamp), Table.CAMP);
            campEditFragment.show(getSupportFragmentManager(), "campDialog");
        } else if (id == REST_PLACE.getValue()) {
            EditFragment<RestPlace> restPlaceEditFragment = new EditFragment<>();
            restPlaceEditFragment.setClass(RestPlace.class, getString(R.string.rest_places),
                    database.restPlaceDao(), getString(R.string.addRestPlace), getString(R.string.updateRestPlace), Table.REST_PLACE);
            restPlaceEditFragment.show(getSupportFragmentManager(), "restPlaceDialog");
        } else if (id == TEST.getValue()) {
            EditFragment<Test> testEditFragment = new EditFragment<>();
            testEditFragment.setClass(Test.class, getString(R.string.test),
                    database.testDao(), getString(R.string.addTest), getString(R.string.updateTest), Table.TEST);
            testEditFragment.show(getSupportFragmentManager(), "testDialog");
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

    public static SportDataBase getDatabase() {
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
