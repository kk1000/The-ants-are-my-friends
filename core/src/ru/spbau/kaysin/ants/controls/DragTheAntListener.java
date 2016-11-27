package ru.spbau.kaysin.ants.controls;

import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;

import ru.spbau.kaysin.ants.entities.Ant;
import ru.spbau.kaysin.ants.model.GameWorld;

/**
 * Created by demarkok on 25-Nov-16.
 */

public class DragTheAntListener extends DragListener {
    Array<Vector2> pathToFollow;
    private GameWorld world;
    private Ant ant;
    private boolean enabled = false;

    public DragTheAntListener(GameWorld world) {
        this.world = world;
    }

    @Override
    public void dragStart(InputEvent event, float x, float y, int pointer) {
        Actor actor = world.getStage().hit(x, y, true);
        if (actor instanceof Ant) {
            init((Ant)actor);
        }
        super.dragStart(event, x, y, pointer);
    }

    private void init(Ant ant) {
        this.ant = ant;
        enabled = true;
        ant.setSteeringBehavior(null);
        pathToFollow = new Array<Vector2>();
        ant.getAntWay().init();
        world.setActiveRecovery(false);
        //TODO make 0.2f Ant's field
        world.setEnergy(world.getEnergy() - 0.2f);
    }

    @Override
    public void drag(InputEvent event, float x, float y, int pointer) {
        if (!enabled || world.getEnergy() <= 0) {
            return;
        }
        Vector2 newPoint = new Vector2(x, y);
        if (pathToFollow.size > 0) {
            float segmentLen = newPoint.dst(pathToFollow.get(pathToFollow.size - 1));
            //TODO make 0.0005 Ant's field
            world.setEnergy(world.getEnergy() - 0.0005f * segmentLen);
        }
        pathToFollow.add(newPoint);
        ant.getAntWay().pushPoint(newPoint);
    }

    @Override
    public void dragStop(InputEvent event, float x, float y, int pointer) {
        if (!enabled) {
            return;
        }

        try {
            ant.setSteeringBehavior(
                    new FollowPath<Vector2, LinePath.LinePathParam>(
                            ant,
                            new LinePath<Vector2>(pathToFollow, true), 10)
                            .setArrivalTolerance(0)
                            .setDecelerationRadius(2));
        } catch (Exception e) {
        }
        world.setActiveRecovery(true);
        enabled = false;
    }
}