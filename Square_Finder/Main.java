package main;

import java.util.*;

public class Main {

	public static void main(String[] args){
		System.out.println(calculateSquares(Arrays.asList(
			new Point(0, 2),
			new Point(2, 2),
			new Point(1, 4),
			new Point(1, 0))
		));
	}
	
	public static int calculateSquares(List<Point> points) {
		if(points.size() < 4){ // no squares can be made of less than 4 points
			return 0;
		}
		
		final double precision = 0.0000000001D; // precision of the point distance calculations
		List<List<Integer>> squares = new ArrayList<List<Integer>>(); // list of already found squares (to make sure there are no duplicates)
		
		for(int a = 0; a < points.size(); a++){
			for(int b = 0; b < points.size(); b++){
				if(a == b){ continue; } // don't calculate the distance between the same points
				double distanceAB = Math.sqrt(Math.pow(points.get(a).getX() - points.get(b).getX(), 2) + Math.pow(points.get(a).getY() - points.get(b).getY(), 2));
				
				for(int c = 0; c < points.size(); c++){
					if(a == c || b == c){ continue; } // don't calculate the distance between the same points
					double distanceBC = Math.sqrt(Math.pow(points.get(b).getX() - points.get(c).getX(), 2) + Math.pow(points.get(b).getY() - points.get(c).getY(), 2));
					
					if(Math.abs(distanceAB - distanceBC) <= precision){ // make sure 2 sides are the same length and if so, look for the 3rd side
						for(int d = 0; d < points.size(); d++){
							if(a == d || b == d || c == d){ continue; } // don't calculate the distance between the same points
							double distanceCD = Math.sqrt(Math.pow(points.get(c).getX() - points.get(d).getX(), 2) + Math.pow(points.get(c).getY() - points.get(d).getY(), 2));
							
							if(Math.abs(distanceAB - distanceCD) <= precision){ // make sure 3 sides are the same length and if so, look for the fourth side
								double distanceDA = Math.sqrt(Math.pow(points.get(a).getX() - points.get(d).getX(), 2) + Math.pow(points.get(a).getY() - points.get(d).getY(), 2));
								
								if(Math.abs(distanceAB - distanceDA) <= precision){ // make sure all 4 sides are the same length
									Point vectorAB = new Point(points.get(b).getX() - points.get(a).getX(), points.get(b).getY() - points.get(a).getY());
									Point vectorBC = new Point(points.get(c).getX() - points.get(b).getX(), points.get(c).getY() - points.get(b).getY());
									
									// make sure the angle between 2 sides is 90 degrees, because it could also be a rhombus
									if(vectorAB.getX() * vectorBC.getX() + vectorAB.getY() * vectorBC.getY() == 0){ 
										// a square has been found
										boolean squareExists = false;
											
										for(int i = squares.size() - 1; i >= 0; i--){ // this loop checks if the found square already exists
											if(squares.get(i).contains(a) && squares.get(i).contains(b) 
													&& squares.get(i).contains(c) && squares.get(i).contains(d)){
												squareExists = true;
												break;
											}
										}
										
										if(!squareExists){ squares.add(Arrays.asList(a, b, c, d)); } // add the found square to the existing square list
									}
								}
							}
						}
					}
				}
			}
		}
		
		return squares.size();
	}
	
	public static class Point {
		
		 private final Integer x;
		 private final Integer y;
		 
		 public Point(Integer x, Integer y) {
			 this.x = x;
			 this.y = y;
		 }
		 
		 public Integer getX() {
			 return x;
		 }
		 
		 public Integer getY() {
			 return y;
		 }
	}
}
