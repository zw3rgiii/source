    package project.de.hshl.vcII.utils;

    /**
     * Vector Class to do calculations with
     */
     public class MyVector {
     public double x, y;

     public MyVector(double x, double y){
        this.x = x;
        this.y = y;
     }

     /*
     * Methods for 2D Vectors (e.g. {2, 5}).
     */
    // Calculates the length of a vector
    public static double length(MyVector vec){
            return Math.sqrt(Math.pow(Math.abs(vec.x), 2) + Math.pow(Math.abs(vec.y), 2));
            }

    // Calculates the distance between two vectors
    public static double distance(MyVector v1, MyVector v2){
       return Math.sqrt(Math.pow(v2.x - v1.x, 2) + Math.pow(v2.y - v1.y, 2));
    }

    // Calculates the dot product. (vector * vector) (Skalarprodukt)
    public static double dot(MyVector v1, MyVector v2){
        return (v1.x * v2.x) + (v1.y * v2.y);
    }

    // Calculates the cross product. (vector x vector) (Kreuzprodukt)
    public static double cross(MyVector v1, MyVector v2){
            return (v1.x*v2.y) - (v1.y*v2.x);
            }

    // Method for calculating the angle between two 2D vectors.
    public static double angle(MyVector v1, MyVector v2){
//        MyVector dot = dot(v1, v2);
        return Math.toDegrees(Math.acos( dot(v1, v2) / (length(v1) * length(v2)) ));
    }

    // Norm the MyVector vec (make its length 1)
    public  static MyVector norm(MyVector vec){
        return MyVector.divide(vec, length(vec));
    }

    // Construct and apply a variable Vector
    public static double applyVectorEquation(MyVector ov, MyVector rv, MyVector p) {
        // ov + s * rv
        double s = (p.x - ov.x)/(rv.x);
//        double sy = (p.y - ov.y)/(rv.y);
        if(Double.isNaN(s))
        {
            s = (p.y - ov.y)/(rv.y);
        }
        /*if(Double.isNaN(sy))
        {
            sy = s;
        }
        if(s == sy)*/
        return s;
    }

    // Calculate the orthogonal projection between given vector and shock normal
    public static MyVector orthogonalProjection(MyVector vec, MyVector normal) {
        return MyVector.multiply(normal, MyVector.dot(normal, vec));
    }

    // Method for multiplying two 2D vectors from each other.
    public static MyVector multiply(MyVector vec, double lambda){
       return new MyVector(vec.x * lambda, vec.y * lambda);
    }

    // Method for dividing two 2D vectors from each other.
    public static MyVector divide(MyVector vec, double lambda){
        return new MyVector(vec.x / lambda, vec.y / lambda);
    }

    // Method for adding two 2D vectors to each other.
    public static MyVector add(MyVector v1, MyVector v2){
            return new MyVector(v1.x + v2.x, v1.y + v2.y);
            }
    // Method for adding multiple 2D vectors to each other.
    public static MyVector addMultiple(MyVector ... vecs){
        double x = 0, y = 0;

        for(MyVector v : vecs) {
            x += v.x;
            y += v.y;
        }

        return new MyVector(x,y);
    }

    // Method for subtracting two 2D vectors from each other.
    public static MyVector subtract(MyVector v1, MyVector v2){
        return new MyVector(v2.x - v1.x, v2.y - v1.y);
    }

    @Override
    public String toString() {
        return "(" + Math.round(this.x) +"/"+ Math.round(this.y) + ")";
    }
}