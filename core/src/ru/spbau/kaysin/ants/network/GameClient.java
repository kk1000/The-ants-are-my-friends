package ru.spbau.kaysin.ants.network;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import ru.spbau.kaysin.ants.entities.Apple;
import ru.spbau.kaysin.ants.entities.Blueberry;
import ru.spbau.kaysin.ants.entities.Bonus;
import ru.spbau.kaysin.ants.model.GameWorld;

import java.io.IOException;

public class GameClient {
    private GameWorld gameWorld;
    private Client client;
    private Move move;
    private int enemyId;

    private IGameSession game;
    private GameServer.IIdGenerator generator;
    private GameServer.IPool pool;

    public GameClient() {

        enemyId = -1;

        client = new Client();
        client.start();
        Network.register(client);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Move) {

                    move = (Move) object;

                }
            }

            @Override
            public void connected(Connection connection) {
                generator = ObjectSpace.getRemoteObject(client,1, GameServer.IIdGenerator.class);
                pool = ObjectSpace.getRemoteObject(client, 2, GameServer.IPool.class);
            }

            @Override
            public void idle(Connection connection) {
                super.idle(connection);
            }
        });

    }

    public void connect(final String host, final IConnectionFailureListener callback) {
        new Thread("Connect") {
            public void run () {
                try {
                    client.connect(5000, host, Network.port);
                } catch (IOException ex) {
                    callback.onFailure();
                }
            }
        }.start();
    }

    public int getID() {
        return client.getID();
    }

    public boolean isConnected() {
        return client.isConnected();
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public void sendMove(Move move) {
        client.sendTCP(move);
        game.endOfTurn(client.getID());
    }

    public int getNewIndex() {
        return generator.getId();
    }

    public void init(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void tryToMatch() {
        if (pool != null) {
            enemyId = pool.match(client.getID());
            if (enemyId != -1) {
                game = ObjectSpace.getRemoteObject(client, 3, IGameSession.class);
            }
        }
    }

    public int getEnemyId() {
        return enemyId;
    }

    public Bonus generateRandomBonus() {
        Vector2 position = game.generateRandomPosition(getID());
        Bonus result = null;
        switch (game.generateRandomBonusType(getID())){
            case 0:
                result = new Apple(position.x, position.y);
                break;
            case 1:
                result = new Blueberry(position.x, position.y);
                break;
        }
        return result;
    }

    public interface IConnectionFailureListener {
        void onFailure();
    }
}
