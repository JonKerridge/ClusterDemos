package Mandelbrot

class Mcollect extends groovyParallelPatterns.DataClass {

  int blackCount = 0
  int whiteCount = 0
  int points = 0
  long totalIters = 0

  static String init = "initClass"
  static String collector = "collector"
  static String finalise = "finalise"

  int initClass ( List d){
    return completedOK
  }

  int finalise( List d){
    println "$points, $whiteCount, $blackCount, $totalIters "
    return completedOK
  }

  int collector(Mdata ml){
    int width = ml.colour.size()
    0.upto(width-1){ int w->
      points = points + 1
      if (ml.colour[w] == ml.WHITE) whiteCount = whiteCount + 1
      else blackCount = blackCount + 1
    }
    totalIters += ml.totalIterations
    return completedOK
  }

}
