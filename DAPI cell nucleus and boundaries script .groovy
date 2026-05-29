import qupath.ext.stardist.StarDist2D
import qupath.lib.scripting.QP

// IMPORTANT! Replace this with the path to your StarDist model
def modelPath = "C:/Users/CFIM/QuPath/v0.6/extensions/dsb2018_heavy_augment.pb"

// Build StarDist detector
def stardist = StarDist2D
    .builder(modelPath)
    .channels('DAPI')
    .normalizePercentiles(1, 99)
    .threshold(0.5)
    .pixelSize(0.5)
    .cellExpansion(5)
    .measureShape()
    .measureIntensity()
    .build()

// Use all annotations as parents
def pathObjects = QP.getAnnotationObjects()

def imageData = QP.getCurrentImageData()
if (pathObjects.isEmpty()) {
    QP.getLogger().error("No annotation objects found!")
    return
}

// Run StarDist detection
stardist.detectObjects(imageData, pathObjects)
stardist.close()

// Run object classifier (QuPath v0.6 syntax)
runObjectClassifier("OBJECT CLASSIFIER")

println("Done!")
