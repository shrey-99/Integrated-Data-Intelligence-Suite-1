import datapoints from "../resources/datapoints.json"
export function showCircleData(clicked_circle_class_name) {
    console.log("I am new function that's running on click");
    //Find the all statistic fields
    const x = document.querySelectorAll("td.ant-descriptions-item.map_1 " +
        "> div.ant-descriptions-item-container" +
        " > span.ant-descriptions-item-content"
    );
    console.log("X-brooo:" + x);

    //Find element with same classname in the datapoints.json file
    let circle_obj_with_data = datapoints.find(x => x.classname === clicked_circle_class_name);
    console.log(circle_obj_with_data);


    //Changing the Value of each Statistic field
    //x[statistic number]
    x[0].innerHTML= circle_obj_with_data.statistic_1;
    x[1].innerHTML= circle_obj_with_data.statistic_2;
    x[2].innerHTML= circle_obj_with_data.statistic_3;
}