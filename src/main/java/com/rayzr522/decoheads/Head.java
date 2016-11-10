/**
 * 
 */
package com.rayzr522.decoheads;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.rayzr522.decoheads.util.CustomHead;

/**
 * @author Rayzr
 *
 */
public class Head {

    private String name;
    private String uuid;
    private String texture;

    private Head(String name, String uuid, String texture) {
        this.name = name;
        this.uuid = uuid;
        this.texture = texture;
    }

    /**
     * @param name the name of the head
     * @param data the head data
     */
    public static Head deserialize(String name, ConfigurationSection data) {
        if (!data.isString("uuid") || !data.isString("texture")) {
            return null;
        }
        return new Head(name, data.get("uuid").toString(), data.get("texture").toString());
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uuid", uuid);
        map.put("texture", texture);
        return map;
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @return the texture
     */
    public String getTexture() {
        return texture;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    void setName(String name) {
        this.name = name;
    }

    public ItemStack getItem() {
        return CustomHead.getHead(texture, uuid, name);
    }

    @Override
    public String toString() {
        return "Head [name=" + name + ", uuid=" + uuid + ", texture=" + texture + "]";
    }

}
