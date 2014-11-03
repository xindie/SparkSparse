package sparse

import org.apache.spark.mllib.linalg.distributed.{MatrixEntry, CoordinateMatrix}
import org.apache.spark.mllib.linalg.{Vectors, Vector}
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.log4j.Level
import org.apache.log4j.Logger
import java.lang.Thread

object TestClient {
    def main(args: Array[String]): Unit = {
        val files = List("fidap005.mtx", "fidapm05.mtx", "pores_1.mtx")
        val filePath = "/Users/Vincent/Documents/GSI/MATH221/Project/"
        val conf = new SparkConf().setAppName("SparseMatrix")
        val sc = new SparkContext(conf)
        Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
        Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)

        val IOobject = new MatrixVectorIO(filePath + "matrices/", sc)
        val outputPath = filePath + "output/"
        val vectorPath = filePath + "vectors/"

        for( i <- 0 until files.length) {
            System.out.println("\n\n\n")
            System.out.println("Reading in matrix: " + files(i))
            val matrix = IOobject.readMatrix(files(i))
            val length = matrix.numCols
            System.out.println("Testing on normal random vector with mean 0, variance 1...")
            val vector = SparseUtility.randomVector(0, 1, length)
            IOobject.saveResult(vector, vectorPath + files(i) + "_rd.txt")
            val outputName = files(i) + "_rd.txt"

            val output = SparseUtility.multiply(matrix, vector, sc)
            IOobject.saveResult(output, outputPath + outputName)
            System.out.println("Test finished, result saved as " + outputPath + outputName)
        }

        Thread.sleep(10000)
    }
}