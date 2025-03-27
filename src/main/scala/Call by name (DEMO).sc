import java.lang.System.nanoTime

def isEqual(x: => Long): Boolean = x == x

isEqual(5)

isEqual(nanoTime())

