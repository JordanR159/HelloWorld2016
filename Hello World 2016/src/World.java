import java.util.ArrayList;

import org.joml.Vector4d;
import org.lwjgl.glfw.GLFW;

public class World 
{
	private int width;
	private int height;
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private Beam projectile = null;

	public World(int numPlayers, int numBlocks)
	{
		width = 1902;
		height = 950;
		setPlayers(numPlayers);
		blocks = WorldGenerator.generateBlocks(width, height, numBlocks);
	}

	public void setPlayers(int numPlayers)
	{
		Player one = new Player(100.00, height-100.00, 100.00);
		Player two = new Player(width-300.00, height-100.00, 100.00);
		players.add(one);
		players.add(two);
		/*for(int i = 0; i < numPlayers; i++)
		{
			double posX = (width*(i+1))/(numPlayers+1)-50.00;
			double posY = height-100.00;
			Player curr = new Player(posX,posY,100.00);
			players.add(curr);
		}*/
	}

	public double getWidth()
	{
		return width;
	}

	public double getHeight()
	{
		return height;
	}

	public void update(double delta)
	{
		//gravity for both players
		for(int i = 0; i < players.size(); i++)
		{
			Player fall = players.get(i);
			fall.setVelY(fall.getVelY()+3);
			double newPos = fall.getVelY() + fall.getPosY();
			if(newPos > 0 && newPos+fall.getSize() < height) {
				boolean collided = false;
				//block collision
				for(int j = 0; j < blocks.size(); j++)
				{
					Block check = blocks.get(j);
					if(newPos < check.getPosY() + check.getHeight() && 
							newPos + fall.getSize() > check.getPosY() && 
							fall.getPosX() < check.getPosX()+check.getWidth() && 
							fall.getPosX() + fall.getSize() > check.getPosX()){
						fall.setPosY(check.getPosY()+fall.getSize());
						fall.resetJumps();
						collided = true;//
					}
				}
				if(!collided)
					fall.setPosY(newPos);
			}
			else if(newPos <= 0)
			{
				fall.setPosY(0);
				fall.setVelY(0);
			}
			else
			{
				fall.setPosY(height-fall.getSize());
				fall.setVelY(0);
				fall.resetJumps();
			}
			players.set(i,fall);
		}
	}
	public int keyPressed(int keyId, int mods)
	{
		Player player = players.get(0);

		//GLFW.GLFW_KEY_<TYPE THE KEY OUT>
		if(keyId == GLFW.GLFW_KEY_W && player.getNumJumps() > 0) //player 1
		{
			playerJump(player);
			return 1;
		}

		if(keyId == GLFW.GLFW_KEY_A) //player 1
		{
			Player curr = players.get(0);

			curr.setVelX(curr.getVelX()-1);
			double newPos = curr.getVelX() + curr.getPosX();
			if(newPos > 0 && newPos+curr.getSize() < width) {

				//block collision
				boolean collided = false;
				for(int j = 0; j < blocks.size(); j++)
				{
					Block check = blocks.get(j);
					if(curr.getPosY() < check.getPosY() + check.getHeight() && 
							curr.getPosY() + curr.getSize() > check.getPosY() && 
							newPos < check.getPosX()+check.getWidth() && 
							newPos + curr.getSize() > check.getPosX()){
						curr.setPosX(check.getPosX()+check.getWidth());
						collided = true;
					}
				}
				if(!collided)
					curr.setPosX(newPos);

			}
			else if(newPos <= 0)
			{
				curr.setPosX(0);
				curr.setVelX(0);
			}
			else
			{
				curr.setPosX(width-curr.getSize());
				curr.setVelX(0);
			}
			players.set(0,curr);
			return 1;
		}

		if(keyId == GLFW.GLFW_KEY_S) //player 1
		{
			Player curr = players.get(0);
			curr.setVelY(curr.getVelY()+10);
			double newPos = curr.getVelY() + curr.getPosY();
			if(newPos > 0 && newPos+curr.getSize() < height) {

				boolean collided = false;
				//block collision on jumps
				for(int j = 0; j < blocks.size(); j++)
				{
					Block check = blocks.get(j);
					if(newPos < check.getPosY() + check.getHeight() && 
							newPos + curr.getSize() > check.getPosY() && 
							curr.getPosX() < check.getPosX()+check.getWidth() && 
							curr.getPosX() + curr.getSize() > check.getPosX()){
						curr.setPosY(check.getPosY());
						curr.resetJumps();
						collided = true;
					}
				}
				if(!collided)
					curr.setPosY(newPos);	
			}
			else if(newPos <= 0)
			{
				curr.setPosY(0);
				curr.setVelY(0);
			}
			else
			{
				curr.setPosY(width-curr.getSize());
				curr.setVelY(0);
				curr.resetJumps();
			}
			players.set(0,curr);
			return 1;
		}

		if(keyId == GLFW.GLFW_KEY_D) //player 1
		{
			Player curr = players.get(0);
			curr.setVelX(curr.getVelX()+1);
			double newPos = curr.getVelX() + curr.getPosX();
			if(newPos > 0 && newPos+curr.getSize() < width) {
				//block collision
				boolean collided = false;
				for(int j = 0; j < blocks.size(); j++)
				{
					Block check = blocks.get(j);
					if(curr.getPosY() < check.getPosY() + check.getHeight() && 
							curr.getPosY() + curr.getSize() > check.getPosY() && 
							newPos < check.getPosX()+check.getWidth() && 
							newPos + curr.getSize() > check.getPosX()){
						curr.setPosX(check.getPosX()-curr.getSize());
						collided = true;
					}
				}
				if(!collided)
					curr.setPosX(newPos);

			}
			else if(newPos <= 0)
			{
				curr.setPosX(0);
				curr.setVelX(0);
			}
			else
			{
				curr.setPosX(width-curr.getSize());
				curr.setVelX(0);
			}
			players.set(0,curr);
			return 1;
		}

		if(keyId == GLFW.GLFW_KEY_Q) //player 1 beam
		{
			Beam shot = new Beam(players.get(0));
			players.get(0).setSize(players.get(0).getSize() - 1);
			if(shot.isOnTarget(players.get(1), blocks)) {
				players.get(1).setSize(players.get(1).getSize() - 3);
			}
			projectile = shot;
			return 1;
		}

		if(keyId == GLFW.GLFW_KEY_TAB) //player 1 explosion
		{




		}

		player = players.get(1);
		if(keyId == GLFW.GLFW_KEY_UP && player.getNumJumps() > 0) //player 2
		{
			playerJump(player);
			return 1;
			/*if(curr.getNumJumps() > 0)
			{
				curr.useJump();
				curr.setVelY(0);
				double newPos = -200.00 + curr.getPosY();
				if(newPos > 0 && newPos+curr.getSize() < height) {
					boolean collided = false;
					//block collision
					for(int j = 0; j < blocks.size(); j++)
					{
						Block check = blocks.get(j);
						if(newPos < check.getPosY() + check.getHeight() && 
								newPos + curr.getSize() > check.getPosY() && 
								curr.getPosX() < check.getPosX()+check.getWidth() && 
								curr.getPosX() + curr.getSize() > check.getPosX()){
							curr.setPosY(check.getPosY() + check.getHeight());
							//can't jump anymore if collide with bottom of block
							curr.nullifyJumps();
							collided = true;
						}
					}
					if(!collided)
						curr.setPosY(newPos);
				}
				else if(newPos <= 0)
					curr.setPosY(0);
				else
				{
					curr.setPosY(height-curr.getSize());
					curr.resetJumps();
				}					
			}
			players.set(1,curr);
			return 1;*/
		}

		if(keyId == GLFW.GLFW_KEY_DOWN) //player 2
		{
			Player curr = players.get(1);
			curr.setVelY(curr.getVelY()+10);
			double newPos = curr.getVelY() + curr.getPosY();
			if(newPos > 0 && newPos+curr.getSize() < height-100.00) {
				boolean collided = false;
				//block collision
				for(int j = 0; j < blocks.size(); j++)
				{
					Block check = blocks.get(j);
					if(newPos < check.getPosY() + check.getHeight() && 
							newPos + curr.getSize() > check.getPosY() && 
							curr.getPosX() < check.getPosX()+check.getWidth() && 
							curr.getPosX() + curr.getSize() > check.getPosX()){
						curr.setPosY(check.getPosY());
						curr.resetJumps();
						collided = true;
					}
				}
				if(!collided)
					curr.setPosY(newPos);
			}
			else if(newPos <= 0)
			{
				curr.setPosY(0);
				curr.setVelY(0);
			}
			else
			{
				curr.setPosY(width-curr.getSize());
				curr.setVelY(0);
				curr.resetJumps();
			}
			players.set(1,curr);
			return 1;
		}

		if(keyId == GLFW.GLFW_KEY_LEFT) //player 2
		{
			Player curr = players.get(1);
			curr.setVelX(curr.getVelX()-1);
			double newPos = curr.getVelX() + curr.getPosX();
			if(newPos > 0 && newPos+curr.getSize() < width) {
				//block collision
				boolean collided = false;
				for(int j = 0; j < blocks.size(); j++)
				{
					Block check = blocks.get(j);
					if(curr.getPosY() < check.getPosY() + check.getHeight() && 
							curr.getPosY() + curr.getSize() > check.getPosY() && 
							newPos < check.getPosX()+check.getWidth() && 
							newPos + curr.getSize() > check.getPosX()){
						curr.setPosX(check.getPosX()+check.getWidth());
						collided = true;
					}
				}
				if(!collided)
					curr.setPosX(newPos);

			}
			else if(newPos <= 0)
			{
				curr.setPosX(0);
				curr.setVelX(0);
			}
			else
			{
				curr.setPosX(width-curr.getSize());
				curr.setVelX(0);
			}
			players.set(1,curr);
			return 1;
		}

		if(keyId == GLFW.GLFW_KEY_RIGHT) //player 2
		{
			Player curr = players.get(1);
			curr.setVelX(curr.getVelX()+1);
			double newPos = curr.getVelX() + curr.getPosX();
			if(newPos > 0 && newPos+curr.getSize() < width) {
				//block collision
				boolean collided = false;
				for(int j = 0; j < blocks.size(); j++)
				{
					Block check = blocks.get(j);
					if(curr.getPosY() < check.getPosY() + check.getHeight() && 
							curr.getPosY() + curr.getSize() > check.getPosY() && 
							newPos < check.getPosX()+check.getWidth() && 
							newPos + curr.getSize() > check.getPosX()){
						curr.setPosX(check.getPosX()-curr.getSize());
						collided = true;
					}
				}
				if(!collided)
					curr.setPosX(newPos);
			}
			else if(newPos <= 0)
			{
				curr.setPosX(0);
				curr.setVelX(0);
			}
			else
			{
				curr.setPosX(width-curr.getSize());
				curr.setVelX(0);
			}
			players.set(1,curr);
			return 1;
		}
		if(keyId == GLFW.GLFW_KEY_SLASH) //player 2 beam
		{
			Beam shot = new Beam(players.get(1));
			players.get(1).setSize(players.get(1).getSize() - 1);
			if(shot.isOnTarget(players.get(0), blocks)) {
				players.get(0).setSize(players.get(0).getSize() - 3);
			}
			projectile = shot;
			return 1;
		}

		if(keyId == GLFW.GLFW_KEY_PERIOD) //player 2 explosion
		{


		}

		return -1;
	}
	
	private void checkCollision(Player player, double newPos)
	{
		boolean collided = false;
		if(newPos > 0 && newPos + player.getSize() < height)
		{
			Block block;
			Vector4d newVecPos = new Vector4d(player.getPosVector());
			newVecPos.y = newPos;
			for(int j = 0; j < blocks.size(); j++)
			{
				block = blocks.get(j);
				if(CollisionHelper.notColliding(newVecPos, block.getVector()))
				{
					collided = true;
				}
			}
		}
	}

	private void playerJump(Player player) 
	{
		player.useJump();
		player.setVelY(-40);
	}

	public int keyHeld(int keyId, int called, int mods)
	{
		return keyPressed(keyId,mods);
	}

	public void keyRelease(int keyId, int mods)
	{

	}


	public ArrayList<Player> getPlayers()
	{
		return players;
	}

	public ArrayList<Block> getBlocks()
	{
		return blocks;
	}

	public Beam getBeam()
	{
		Beam temp = projectile;
		projectile = null;
		return temp;

	}

}
