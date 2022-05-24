package de.jero.pog;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Join implements Listener {

    public static boolean started = false;
    public static boolean gamestarted = false;

    public static int alive;
    public static int survived;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage("§7[§a+§7] " + e.getPlayer().getName());
        e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), 0,10,0));
        e.getPlayer().getWorld().setDifficulty(Difficulty.PEACEFUL);
        int playercount = e.getPlayer().getWorld().getPlayers().size();
        final int[] counter = {30};
        if (playercount >= 1) {
            if (!started) {
                started = true;
                List<Player> players = e.getPlayer().getWorld().getPlayers();
                for (Player p : players) {
                    p.setLevel(30);
                }
                Bukkit.broadcastMessage(Pog.prefix + "§7Das Spiel beginnt in §a30 §7Sekunden!");
                Bukkit.getScheduler().scheduleSyncRepeatingTask(Pog.getInstance(), new Runnable() {
                    public void run() {
                        if (e.getPlayer().getWorld().getPlayers().size() < 2) {
                            started = false;
                            List<Player> players = e.getPlayer().getWorld().getPlayers();
                            Bukkit.broadcastMessage(Pog.prefix + "§7Nicht genügend Spieler!");
                            Bukkit.getScheduler().cancelTasks(Pog.getInstance());
                        }
                        counter[0]--;
                        if (counter[0] == 3 || counter[0] == 2 || counter[0] == 1) {
                            players.get(0).getLocation().getWorld().playSound(players.get(0).getLocation(), Sound.BLOCK_NOTE_FLUTE, 5, 1);
                        }
                        if (counter[0] == 0) {
                            Bukkit.getScheduler().cancelTasks(Pog.getInstance());
                            Bukkit.broadcastMessage(Pog.prefix + "§7Abflug!");
                            poggersSelector(players);
                        }
                        List<Player> players = e.getPlayer().getWorld().getPlayers();
                        for (Player p : players) {
                            p.setLevel(counter[0]);
                        }
                    }
                }, 0L, 20);
            } else {
                e.getPlayer().setLevel(counter[0]);
                e.getPlayer().sendMessage(Pog.prefix + "§7Es läuft momentan eine Runde");
            }
        }
    }

    public void poggersSelector(List<Player> p) {
        alive = 0;
        gamestarted = true;
        List<Player> players = p.get(0).getWorld().getPlayers();
        World world = players.get(0).getWorld();
        Location loc = new Location(world, -213, 19, -1120);
        Location loc2 = new Location(world, -213, 18, -1120);
        loc.getBlock().setType(Material.OBSIDIAN);
        loc2.getBlock().setType(Material.OBSIDIAN);
        world.setDifficulty(Difficulty.PEACEFUL);
        Random random = new Random();
        int rndm = random.nextInt(players.size());
        System.out.println("random:" + rndm);
        Player pogger = players.get(rndm);
        System.out.println("Pogger:" + pogger.getName());
        pogger.sendMessage(Pog.prefix + "§7Du bist der §4§lPogger§7, töte sie alle!");
        pogger.teleport(new Location(pogger.getWorld(), -203, 127, -1164));
        ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) helm.getItemMeta();
        meta.setColor(Color.fromRGB(Color.RED.asRGB()));
        helm.setItemMeta(meta);
        ItemStack brust = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta brustmeta = (LeatherArmorMeta) brust.getItemMeta();
        brustmeta.setColor(Color.fromRGB(Color.RED.asRGB()));
        brust.setItemMeta(brustmeta);
        ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta legmeta = (LeatherArmorMeta) leg.getItemMeta();
        legmeta.setColor(Color.fromRGB(Color.RED.asRGB()));
        leg.setItemMeta(legmeta);
        ItemStack boot = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta bootmeta = (LeatherArmorMeta) boot.getItemMeta();
        bootmeta.setColor(Color.fromRGB(Color.RED.asRGB()));
        boot.setItemMeta(bootmeta);
        pogger.getInventory().setHelmet(helm);
        pogger.getInventory().setChestplate(brust);
        pogger.getInventory().setLeggings(leg);
        pogger.getInventory().setBoots(boot);
        List<Player> normalos = new ArrayList<>();
        for (Player pl : players) {
            if (pl != pogger) {
                pl.getInventory().clear();
                normalos.add(pl);
                alive++;
            }
        }
        System.out.println("Alive: " + alive);
        for (Player n : normalos) {
            n.sendMessage(Pog.prefix + "§7Entkomme aus dem Flugzeug!");
            n.sendMessage(Pog.prefix + "§7Der §4§lPogger §7bricht in 20 Sekunden aus!");
            n.teleport(new Location(n.getWorld(), -241, 23, -1120));
        }
        final int[] counter = {0};
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Pog.getInstance(), new Runnable() {
            public void run() {
                if (counter[0] == 15) {
                    pogger.teleport(new Location(pogger.getWorld(), -249, 18, -1120));
                    pogger.getWorld().playSound(pogger.getLocation(), Sound.ENTITY_BLAZE_DEATH, 5, 0);
                    pogger.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, new Location(world, -249, 19, -1118), 10);
                    Bukkit.broadcastMessage(Pog.prefix + "§7Der §4§lPogger §7ist nun frei!");
                }
                counter[0]++;
            }
        }, 0L, 20);
        spawnOres(world);
    }

    private Vector[] locations = new Vector[] {
            new Vector(-206, 23, -1119),
            new Vector(-206, 20, -1120),
            new Vector(-244, 19, -1121),
            new Vector(-261, 20, -1121),
            new Vector(-260, 23, -1120),
            new Vector(-233, 22, -1150),
            new Vector(-233, 22, -1090),
            new Vector(-215, 23, -1119),
            new Vector(-231, 19, -1120),
            new Vector(-248, 18, -1120),
            new Vector(-235, 24, -1124),
            new Vector(-225, 23, -1122),
            new Vector(-227, 23, -1122),
            new Vector(-229, 23, -1122),
            new Vector(-227, 23, -1118),
            new Vector(-229, 23, -1118),
            new Vector(-238, 23, -1117),
            new Vector(-242, 23, -1117),
            new Vector(-203, 26, -1127),
            new Vector(-252, 23, -1122),
            new Vector(-200, 32, -1120),
            new Vector(-258, 26, -1119),
    };

    private class Key {
        private Material clickable;
        private Material clickWith;
        private String name;

        public Key(Material clickable, Material clickWith, String name) {
            this.clickable = clickable;
            this.clickWith = clickWith;
            this.name = name;
        }
    }

    private Key[] keys = new Key[] {
            new Key(Material.COAL_ORE, Material.COAL, "Kohle"),
            new Key(Material.IRON_ORE, Material.IRON_INGOT, "Eisen"),
            new Key(Material.REDSTONE_ORE, Material.REDSTONE, "Redstone"),
            new Key(Material.GOLD_ORE, Material.GOLD_INGOT, "Gold"),
            new Key(Material.EMERALD_ORE, Material.EMERALD, "Smaragd"),
            new Key(Material.DIAMOND_ORE, Material.DIAMOND, "Diamant"),
            new Key(Material.OBSIDIAN, Material.NETHER_STAR, "Stern")
    };

    public void spawnOres(World w) {
        w.getEntitiesByClass(ArmorStand.class).forEach(ArmorStand::remove);
        w.getEntitiesByClass(Item.class).forEach(Item::remove);

        List<Location> locs = Arrays.stream(locations)
                .map(vec -> vec.toLocation(w))
                .collect(Collectors.toList());

        for (Location location : locs) {
            location.getBlock().setType(Material.AIR);
        }

        Random random = new Random();
        for (int i = 0; i < keys.length - 1; i++) {
            Location location = locs.remove(random.nextInt(locs.size()));
            location.getBlock().setType(keys[i].clickable);
        }

        Location location = locs.remove(random.nextInt(locs.size()));
        itemSpawner(w, location, new ItemStack(keys[0].clickWith), keys[0].name);
        snowBall(w);
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage("§7[§c-§7] " + e.getPlayer().getName());
        //Pogger abgehauen
        if (e.getPlayer().getInventory().getHelmet() != null) {
            Bukkit.getScheduler().cancelTasks(Pog.getInstance());
            Bukkit.broadcastMessage(Pog.prefix + "§7Der §4§lPogger §7ist abgehauen!");
            for(Player p : e.getPlayer().getWorld().getPlayers()) {
                p.teleport(new Location(p.getWorld(), 0,10,0));
            }
            restart(e.getPlayer().getWorld());
        } else {
            alive--;
            if(alive == 0) {
                Bukkit.broadcastMessage(Pog.prefix + "§7Der letzte Überlebende ist gegangen!");
                for(Player p : e.getPlayer().getWorld().getPlayers()) {
                    p.teleport(new Location(p.getWorld(), 0,10,0));
                }
                restart(e.getPlayer().getWorld());
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Material clickType = e.getClickedBlock().getType();
            Material invType = e.getPlayer().getInventory().getItemInMainHand().getType();

            boolean success = false;
            for (int i = 0; i < keys.length - 1; i++) {
                if (clickType == keys[i].clickable && invType == keys[i].clickWith) {
                    itemSpawner(e.getClickedBlock().getWorld(), e.getClickedBlock().getLocation(), new ItemStack(keys[i + 1].clickWith), keys[i + 1].name);
                    success = true;
                    break;
                }
            }

            if (clickType == keys[keys.length - 1].clickable && invType == keys[keys.length - 1].clickWith) {
                Key key = keys[keys.length - 1];
                e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                e.getClickedBlock().getWorld().playSound(e.getClickedBlock().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 5);
                e.getClickedBlock().setType(Material.AIR);
                Location loc1 = e.getClickedBlock().getLocation().clone();
                if (loc1.add(0, 1, 0).getBlock().getType().equals(key.clickable)) {
                    loc1.getBlock().setType(Material.AIR);
                }
                Location loc2 = e.getClickedBlock().getLocation().clone();
                if (loc2.add(0,-1,0).getBlock().getType().equals(key.clickable)) {
                    loc2.getBlock().setType(Material.AIR);
                }
            }

            if (success) {
                e.getClickedBlock().setType(Material.AIR);
                Bukkit.getOnlinePlayers().forEach(p -> p.getInventory().remove(invType));
            }
        }
    }

    private ArmorStand armorStand;
    private BukkitTask spawnerTask;

    public void itemSpawner(World world, Location loc, ItemStack item, String name) {
        if (spawnerTask != null) spawnerTask.cancel();
        if (armorStand != null) armorStand.remove();

        armorStand = (ArmorStand) world.spawnEntity(loc.clone().add(0.5,0,0.5), EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setCustomNameVisible(true);
        armorStand.setSmall(true);

        final int[] countdown = {1};

        spawnerTask = Bukkit.getScheduler().runTaskTimer(Pog.getInstance(), new Runnable() {
            public void run() {
                countdown[0]--;
                armorStand.setCustomName("§a§l" + name + " §7in §a" + countdown[0] + " §7Sekunden");
                if (countdown[0] == 0) {
                    countdown[0] = 31;

                    world.getEntitiesByClass(Item.class).forEach(item -> {
                        for (Key key : keys) {
                            if (key.clickWith == item.getItemStack().getType()) {
                                item.remove();
                                break;
                            }
                        }
                    });
                    world.dropItem(armorStand.getLocation(), item);
                }
            }
        }, 0L, 20);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        p.spigot().respawn();
        if (e.getEntity().getInventory().getHelmet() == null) {
            alive--;
            e.getDrops().clear();
            e.setDeathMessage(Pog.prefix + "§7Den §d" + e.getEntity().getName() + " §7hats zerlegt :(");
            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_COW_HURT, 5, 5);
            if (alive == 0) {
                Bukkit.broadcastMessage(Pog.prefix + "§7Alle sind tot! Der §4§lPogger §7hat gewonnen!");
                Bukkit.getScheduler().scheduleSyncDelayedTask(Pog.getInstance(), new Runnable() {
                    public void run() {
                        restart(p.getWorld());
                    }
                }, 20 * 5L);
            }
        } else {
            e.getDrops().clear();
            Bukkit.broadcastMessage(Pog.prefix + "§7Den §4§lPogger §7hats zerlegt. Die Überlebenden haben gewonnen!");
            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 5, 1);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Pog.getInstance(), new Runnable() {
                public void run() {
                    restart(p.getWorld());
                }
            }, 20 * 5L);
        }
    }

    @EventHandler
    public void onausziehen(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getSlotType().equals(InventoryType.SlotType.ARMOR)) {
            ItemStack item = e.getCursor();
            e.setCancelled(true);
        }
    }
    public static boolean moveable = true;
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player p1 = (Player) e.getEntity();
            Player p2 = (Player) e.getDamager();
            if (p2.getInventory().getHelmet() != null) {
                if(moveable) {
                    e.setDamage(1000);
                } else {
                    e.setCancelled(true);
                }
            } else {
                System.out.println("test");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void pickUp(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        if (e.getPlayer().getInventory().getHelmet() != null) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(p.getInventory().getHelmet() != null) {
            if(!moveable) {
                e.setCancelled(true);
            }
        }
        if (e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.EMERALD_BLOCK)) {
            if(e.getPlayer().getInventory().getHelmet() == null) {
                p.setGameMode(GameMode.SPECTATOR);
                alive--;
                if (alive == 0) {
                    Bukkit.broadcastMessage(Pog.prefix + "§7Die Überlebenden haben gewonnen!");
                    restart(p.getWorld());
                }
            }
        }
    }

    public void restart(World world) {
        Bukkit.getScheduler().cancelTasks(Pog.getInstance());
        for(Player p : world.getPlayers()) {
            p.teleport(new Location(world, 0,10,0));
            p.getInventory().clear();
            p.setGameMode(GameMode.SURVIVAL);
        }
        Bukkit.broadcastMessage(Pog.prefix + "§7Das Spiel beginnt in §a30 §7Sekunden!");
        alive = 0;
        Location loc = new Location(world, -213, 19, -1120);
        Location loc2 = new Location(world, -213, 18, -1120);
        loc.getBlock().setType(Material.OBSIDIAN);
        loc2.getBlock().setType(Material.OBSIDIAN);

        final int[] counter = {30};
        started = true;
        List<Player> players = world.getPlayers();
        for (Player p : players) {
            p.setLevel(30);
            p.getInventory().clear();
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Pog.getInstance(), new Runnable() {
            public void run() {
                if (world.getPlayers().size() < 2) {
                    started = false;
                    List<Player> players = world.getPlayers();
                    Bukkit.broadcastMessage(Pog.prefix + "§7Nicht genügend Spieler!");
                    Bukkit.getScheduler().cancelTasks(Pog.getInstance());
                }
                counter[0]--;
                if (counter[0] == 3 || counter[0] == 2 || counter[0] == 1) {
                    players.get(0).getLocation().getWorld().playSound(players.get(0).getLocation(), Sound.BLOCK_NOTE_FLUTE, 5, 1);
                }
                if (counter[0] == 0) {
                    Bukkit.getScheduler().cancelTasks(Pog.getInstance());
                    Bukkit.broadcastMessage(Pog.prefix + "§7Abflug!");
                    poggersSelector(players);
                }
                List<Player> players = world.getPlayers();
                for (Player p : players) {
                    p.setLevel(counter[0]);
                }
            }
        }, 0L, 20);
    }
    private ArmorStand armor;
    private BukkitTask snowballTask;
    public void snowBall(World world) {
        System.out.println("angekommen");
        Location loca = new Location(world, -234.5,24,-1120.5);
        armor = (ArmorStand) world.spawnEntity(loca, EntityType.ARMOR_STAND);
        armor.setVisible(false);
        armor.setGravity(false);
        armor.setInvulnerable(true);
        armor.setCustomNameVisible(true);
        armor.setSmall(true);

        final int[] snowdown = {1};

        snowballTask = Bukkit.getScheduler().runTaskTimer(Pog.getInstance(), new Runnable() {
            public void run() {
                snowdown[0]--;
                armor.setCustomName("§b§lSchneeball §7in §b" + snowdown[0] + " §7Sekunden");
                if (snowdown[0] == 0) {
                    snowdown[0] = 61;
                    List<Entity> entities = world.getEntities();
                    for(Entity entity : entities) {
                        if(entity instanceof Item) {
                            System.out.println(entity.getName());
                            if(entity.getName().equals("item.item.snowball")) {
                                entity.remove();
                            }
                        }
                    }
                    world.dropItem(loca, new ItemStack(Material.SNOW_BALL));
                }
            }
        }, 0L, 20);
    }
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if(e.getEntity() instanceof Snowball) {
            if(e.getHitEntity() instanceof Player) {
                Player p = (Player) e.getHitEntity();
                if(p.getInventory().getHelmet() != null) {
                    moveable = false;
                    Bukkit.broadcastMessage(Pog.prefix + "§7Der §4§lPogger §7wurde eingefroren!");
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5, 5);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Pog.getInstance(), new Runnable() {
                        public void run() {
                            moveable = true;
                        }
                    }, 20 * 20L);
                }
            }
        }
    }
    @EventHandler
    public void respawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(new Location(e.getPlayer().getWorld(), 0,10,0));
    }
}
