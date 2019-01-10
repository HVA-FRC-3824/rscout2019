package frc3824.rscout2018.views;

/**
 * @interface ScoutHeaderInterface
 *
 * An interface which holds the functions for the ScoutHeader
 */
public interface ScoutHeaderInterface {

    /**
     * The function that should run when the previous button on {@link ScoutHeader} is clicked
     */
    void previous();

    /**
     * The function that should run when the next button on {@link ScoutHeader} is clicked
     */
    void next();

    /**
     * The function that should run when the home button on {@link ScoutHeader} is clicked
     */
    void home();

    /**
     * The function that should run when the list button on {@link ScoutHeader} is clicked
     */
    void list();

    /**
     * The function that should run when the save button on {@link ScoutHeader} is clicked
     */
    void save();
}
