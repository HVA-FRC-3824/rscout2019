package frc3824.rscout2018.custom_charts.lld;

import com.github.mikephil.charting.utils.Transformer;

public class LLDTransformer
{
    Transformer mTransformer;

    public LLDTransformer(Transformer transformer)
    {
        mTransformer = transformer;
    }

    float[] valuePointsForGenerateTransformedValuesLLD = new float[1];

    /**
     * Transforms an List of Entry into a float array containing the x and
     * y values transformed with all matrices for the LLD_Chart.
     *
     * @param data
     * @return
     */
    public float[] generateTransformedValuesLLD(ILLDDataSet data, float phaseX, float phaseY, int from, int to) {

        final int count = (int) ((to - from) * phaseX + 1) * 2;

        if(valuePointsForGenerateTransformedValuesLLD.length != count){
            valuePointsForGenerateTransformedValuesLLD = new float[count];
        }
        float[] valuePoints = valuePointsForGenerateTransformedValuesLLD;

        for (int j = 0; j < count; j += 2) {

            LLDEntry e = data.getEntryForIndex(j / 2 + from);

            if (e != null) {
                valuePoints[j] = e.getXIndex();
                valuePoints[j + 1] = e.getMax() * phaseY;
            }else{
                valuePoints[j] = 0;
                valuePoints[j + 1] = 0;
            }
        }

        mTransformer.getValueToPixelMatrix().mapPoints(valuePoints);

        return valuePoints;
    }
}
