package com.pantryoncommand.command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * Object for image objects
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image{
    byte[] content;
    String path;
    String name;
}
