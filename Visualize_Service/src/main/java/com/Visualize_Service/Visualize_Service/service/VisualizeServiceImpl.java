package com.Visualize_Service.Visualize_Service.service;

import com.Visualize_Service.Visualize_Service.dataclass.*;
import com.Visualize_Service.Visualize_Service.exception.InvalidRequestException;
import com.Visualize_Service.Visualize_Service.request.*;
import com.Visualize_Service.Visualize_Service.response.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VisualizeServiceImpl {

    private HashSet<String> filterdCloud = new HashSet<String>();



    public VisualizeDataResponse visualizeData(VisualizeDataRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("FindEntitiesRequest Object is null");
        }
        if (request.getAnomalyList() == null){
            throw new InvalidRequestException("Arraylist of AnomalyList is null");
        }
        /*if (request.getPatternList() == null){
            throw new InvalidRequestException("Arraylist of PatternList is null");
        }
        if (request.getPredictionList() == null){
            throw new InvalidRequestException("Arraylist of PredictionList is null");
        }*/
        if (request.getRelationshipList() == null){
            throw new InvalidRequestException("Arraylist of RelationshipList is null");
        }
        if (request.getTrendList() == null){
            throw new InvalidRequestException("Arraylist of TrendList is null");
        }

        ArrayList<ArrayList> outputData = new ArrayList<>();
        setCloud();


        //***********************Overview Section*************************//

        //total likes                                     <- D
        //most prominent Sentiment                        <- D
        //Number of trends                                <- D
        //NUmber of anomalys                              <- D

        //Bar Graph(Average Interaction)                  <- D
        //PieChart(Overall section)                       <- D
        // Bar Graph(total Engagement in each location)   <- *

        //map                                             <- D
        //frequecy of tweets in trend                     <- D

        //word cloud                                      <- D
        //word cloud pie chart                            <-
        //word cloud sunburst                             <-

        //Network graph1(Relationships)                   <- D
        // Network graph 2(Patterns)                      <-

        //Timeline(Anomaly)                               <- D
        // scatter plot * tentative                       <-
        // Map Metric 2 (Number of tweets over time )     <- D

        //Map graph
        CreateMapGraphRequest mapRequest = new CreateMapGraphRequest(request.getTrendList());
        CreateMapGraphResponse mapResponse =  this.createMapGraph(mapRequest);
        outputData.add(mapResponse.mapGraphArray);

        //Network graph
        CreateNetworkGraphRequest networkRequest = new CreateNetworkGraphRequest(request.getRelationshipList());
        CreateNetworkGraphResponse networkResponse =  this.createNetworkGraph(networkRequest);
        outputData.add(networkResponse.NetworkGraphArray);

        //Timeline graph
        CreateTimelineGraphRequest timelineRequest = new CreateTimelineGraphRequest(request.getAnomalyList());
        CreateTimelineGraphResponse timelineResponse =  this.createTimelineGraph(timelineRequest);
        outputData.add(timelineResponse.timelineGraphArray);

        //Line graph Sentiments
        CreateLineGraphSentimentsRequest lineRequest = new CreateLineGraphSentimentsRequest(request.getTrendList());
        CreateLineGraphSentimentsResponse lineResponse =  this.createLineGraphSentiments(lineRequest);
        outputData.add(lineResponse.LineGraphArray);

        //Line graph Interactions
        CreateLineGraphInteractionsRequest lineInteractionsRequest = new CreateLineGraphInteractionsRequest(request.getTrendList());
        CreateLineGraphInteractionsResponse lineInteractionsResponse =  this.createLineGraphInteractions(lineInteractionsRequest);
        outputData.add(lineInteractionsResponse.LineGraphArray);

        //Bar graph
        CreateBarGraphRequest barRequest = new CreateBarGraphRequest(request.getTrendList());
        CreateBarGraphResponse barResponse =  this.createBarGraph(barRequest);
        outputData.add(barResponse.BarGraphArray);

        //PieChart graph
        CreatePieChartGraphRequest pieChartRequest = new CreatePieChartGraphRequest(request.getTrendList());
        CreatePieChartGraphResponse pieChartResponse =  this.createPieChartGraph(pieChartRequest);
        outputData.add(pieChartResponse.PieChartGraphArray);

        //WordCloud graph
        CreateWordCloudGraphRequest wordCloudRequest = new CreateWordCloudGraphRequest(request.getTrendList());
        CreateWordCloudGraphResponse wordCloudResponse = this.createWordCloudGraph(wordCloudRequest);
        outputData.add(wordCloudResponse.words);


        //ExtraBar graph one
        CreateBarGraphExtraOneRequest extraBarOneRequest = new CreateBarGraphExtraOneRequest(request.getTrendList());
        CreateBarGraphExtraOneResponse extraBarOneResponse = this.createBarGraphExtraOne(extraBarOneRequest);
        outputData.add(extraBarOneResponse.BarGraphArray);

        //ExtraBar graph two
        CreateBarGraphExtraTwoRequest extraBarTwoRequest = new CreateBarGraphExtraTwoRequest(request.getTrendList());
        CreateBarGraphExtraTwoResponse extraBarTwoResponse = this.createBarGraphExtraTwo(extraBarTwoRequest);
        outputData.add(extraBarTwoResponse.BarGraphArray);


        return new VisualizeDataResponse( outputData );
    }


    public GetTotalInteractionResponse getTotalInteraction(GetTotalInteractionRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("CreateTimelineGraphRequest Object is null");
        }
        if (request.getDataList() == null) {
            throw new InvalidRequestException("Arraylist is null");
        }
        String outputs = "";
        ArrayList<ArrayList> reqData = request.getDataList();
        ArrayList<Graph> output = new ArrayList<>();
        int Tot = 0;
        for (int i = 0; i < reqData.size(); i++) {
            ArrayList<String> Like = (ArrayList<String>) reqData.get(i).get(7);
            for (String lk : Like){
                Tot += Integer.parseInt(lk);
            }
        }
        WordCloudGraph out = new WordCloudGraph();
        out.words = String.valueOf(Tot);
        output.add(out);
        System.out.println(out.words);
        return new GetTotalInteractionResponse(output);
    }

    public GetMostProminentSentimentResponse getMostProminentSentiment(GetMostProminentSentimentRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("CreateTimelineGraphRequest Object is null");
        }
        if (request.getDataList() == null) {
            throw new InvalidRequestException("Arraylist is null");
        }
        String outputs = "";
        ArrayList<ArrayList> reqData = request.getDataList();
        ArrayList<Graph> output = new ArrayList<>();

        int k = 0;
        ArrayList<String> listSent = new ArrayList<>();
        ArrayList<ArrayList> out = new ArrayList<>();
        for (int i = 0; i < reqData.size(); i++) {
            ArrayList<String> sents = (ArrayList<String>) reqData.get(i).get(4);
            //System.out.println(locs.toString());

            for (int j = 0; j < sents.size(); j++) {
                if (listSent.isEmpty()){
                    listSent.add(sents.get(j));
                    ArrayList<Object> r = new ArrayList<>();
                    r.add(sents.get(j));
                    r.add(1);
                    out.add(r);
                }else {
                    if (listSent.contains(sents.get(j))){
                        ArrayList<Object>r =  out.get(listSent.indexOf(sents.get(j)));
                        int val=Integer.parseInt(r.get(1).toString());
                        val++;
                        r.set(1,val);
                        out.set(listSent.indexOf(sents.get(j)),r);
                    }else {
                        listSent.add(sents.get(j));
                        ArrayList<Object> r = new ArrayList<>();
                        r.add(sents.get(j));
                        r.add(1);
                        out.add(r);
                    }
                }
            }
        }


        outputs = out.get(0).get(0).toString();
        int temp = Integer.parseInt(out.get(0).get(1).toString());
        for (ArrayList o : out) {
            //System.out.println(o);
            if (Integer.parseInt(o.get(1).toString()) > temp){
                outputs = o.get(0).toString();
                temp = Integer.parseInt(o.get(1).toString());
            }
        }

        WordCloudGraph outW = new WordCloudGraph();
        outW.words=outputs;
        output.add(outW);
        System.out.println(outputs);
        return new GetMostProminentSentimentResponse(output);
    }

    public GetTotalTrendsResponse getTotalTrends(GetTotalTrendsRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("CreateTimelineGraphRequest Object is null");
        }
        if (request.getDataList() == null) {
            throw new InvalidRequestException("Arraylist is null");
        }
        ArrayList<ArrayList> reqData = request.getDataList();
        ArrayList<Graph> output = new ArrayList<>();

        WordCloudGraph out = new WordCloudGraph();
        out.words = String.valueOf(reqData.size());
        output.add(out);
        System.out.println(out.words);
        return new GetTotalTrendsResponse(output);
    }

    public GetTotalAnomaliesResponse getTotalAnomalies(GetTotalAnomaliesRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("CreateTimelineGraphRequest Object is null");
        }
        if (request.getDataList() == null) {
            throw new InvalidRequestException("Arraylist is null");
        }
        ArrayList<String> reqData = request.getDataList();
        ArrayList<Graph> output = new ArrayList<>();

        WordCloudGraph out = new WordCloudGraph();
        out.words = String.valueOf(reqData.size());
        output.add(out);
        System.out.println(out.words);
        return new GetTotalAnomaliesResponse(output);
    }




    public CreateTimelineGraphResponse createTimelineGraph(CreateTimelineGraphRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("CreateTimelineGraphRequest Object is null");
        }
        if (request.getDataList() == null){
            throw new InvalidRequestException("Arraylist is null");
        }

        ArrayList<String> reqData = request.getDataList();
        ArrayList<Graph> output = new ArrayList<>();
        for (int i = 0; i < reqData.size(); i++) {
            TimelineGraph newGraph = new TimelineGraph();

            Random random = new Random();
            int minDay = (int) LocalDate.of(2021, 03, 1).toEpochDay();
            int maxDay = (int) LocalDate.now().toEpochDay();
            long randomDay = minDay + random.nextInt(maxDay - minDay);

            LocalDate randomBirthDate = LocalDate.ofEpochDay(randomDay);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            String stringDate=randomBirthDate.format(formatter);

            newGraph.title = stringDate;
            newGraph.cardTitle = "Anomaly Detected";
            newGraph.cardSubtitle = reqData.get(i);

            output.add(newGraph);
        }
        return new CreateTimelineGraphResponse(output);
    }

    public CreateLineGraphSentimentsResponse createLineGraphSentiments(CreateLineGraphSentimentsRequest request) throws InvalidRequestException{
        if (request == null) {
            throw new InvalidRequestException("FindEntitiesRequest Object is null");
        }
        if (request.getDataList() == null){
            throw new InvalidRequestException("Arraylist is null");
        }
        ArrayList<ArrayList> reqData = request.getDataList();
        ArrayList<Graph> output = new ArrayList<>();

        int k = 0;
        ArrayList<String> listSent = new ArrayList<>();
        ArrayList<ArrayList> out = new ArrayList<>();
        for (int i = 0; i < reqData.size(); i++) {
            ArrayList<String> sents = (ArrayList<String>) reqData.get(i).get(4);
            //System.out.println(locs.toString());
            listSent = new ArrayList<>();
            out = new ArrayList<>();
            for (int j = 0; j < sents.size(); j++) {
                if (listSent.isEmpty()){
                    listSent.add(sents.get(j));
                    ArrayList<Object> r = new ArrayList<>();
                    r.add(sents.get(j));
                    r.add(1);
                    out.add(r);
                }else {
                    if (listSent.contains(sents.get(j))){
                        ArrayList<Object>r =  out.get(listSent.indexOf(sents.get(j)));
                        int val=Integer.parseInt(r.get(1).toString());
                        val++;
                        r.set(1,val);
                        out.set(listSent.indexOf(sents.get(j)),r);
                    }else {
                        listSent.add(sents.get(j));
                        ArrayList<Object> r = new ArrayList<>();
                        r.add(sents.get(j));
                        r.add(1);
                        out.add(r);
                    }
                }
            }
            int temp = 0;
            String sent = "";
            for (ArrayList o : out) {
               if (temp < Integer.parseInt(o.get(1).toString())) {
                   temp = Integer.parseInt(o.get(1).toString());
                   sent = o.get(0).toString();
               }
            }

            float tot = sents.size() +1;
            float number = ((float)temp)/tot * 50;
            if (sent == "Positive")
                number *= 2;
            else
                number = 100 - number*2;

            LineGraph outp = new LineGraph();
            outp.x = String.valueOf(i);
            outp.y = String.valueOf((int) number);

            System.out.println("x: "+outp.x);
            System.out.println("y: "+outp.y);
            output.add(outp);

        }




        return new CreateLineGraphSentimentsResponse(output);
    }

    public CreateLineGraphInteractionsResponse createLineGraphInteractions(CreateLineGraphInteractionsRequest request) throws InvalidRequestException{
        if (request == null) {
            throw new InvalidRequestException("Request Object is null");
        }
        if (request.getDataList() == null){
            throw new InvalidRequestException("Arraylist is null");
        }
        ArrayList<ArrayList> reqData = request.getDataList();
        ArrayList<Graph> output = new ArrayList<>();

        int k = 0;

        for (int i = 0; i < reqData.size(); i++) {
            float number = Float.parseFloat(reqData.get(i).get(3).toString());

            LineGraph outp = new LineGraph();
            outp.x = String.valueOf(i);
            outp.y = String.valueOf((int) number);

            System.out.println("x: "+outp.x);
            System.out.println("y: "+outp.y);
            output.add(outp);

        }




        return new CreateLineGraphInteractionsResponse(output);
    }



    //frequecy of tweets in trend
    public CreateBarGraphResponse createBarGraph(CreateBarGraphRequest request) throws InvalidRequestException{
        if (request == null) {
            throw new InvalidRequestException("Request Object is null");
        }
        if (request.getDataList() == null){
            throw new InvalidRequestException("Arraylist is null");
        }
        ArrayList<ArrayList> reqData = request.getDataList();
        ArrayList<Graph> output = new ArrayList<>();

        int k = 0;

        for (int i = 0; i < reqData.size(); i++) {
            float number = Float.parseFloat(reqData.get(i).get(6).toString());

            BarGraph outp = new BarGraph();
            outp.x = reqData.get(i).get(0).toString();
            outp.y = String.valueOf((int) number);

            System.out.println("x: "+outp.x);
            System.out.println("y: "+outp.y);
            output.add(outp);

        }




        return new CreateBarGraphResponse(output);
    }

    //Fregency of entity type
    public CreateBarGraphExtraOneResponse createBarGraphExtraOne(CreateBarGraphExtraOneRequest request) throws InvalidRequestException {

        if (request == null) {
            throw new InvalidRequestException("Request Object is null");
        }
        if (request.getDataList() == null){
            throw new InvalidRequestException("Arraylist is null");
        }
        ArrayList<ArrayList> reqData = request.getDataList();

        ArrayList<Graph> output = new ArrayList<>();
        ArrayList<String> types = new ArrayList<>();
        ArrayList<Integer> typeFreq = new ArrayList<>();

        for (int i = 0; i < reqData.size(); i++) {
            String type = reqData.get(i).get(2).toString();
            if (types.contains(type)){
                int freq = typeFreq.get(types.indexOf(type));
                freq++;
                typeFreq.set(types.indexOf(type),freq);
            }else {
                types.add(type);
                typeFreq.add(1);
            }
        }
        BarGraph bar2;
        for (int j = 0; j < types.size(); j++) {
            bar2 = new BarGraph();
            bar2.x = types.get(j).toString();
            bar2.y = typeFreq.get(j).toString();

            System.out.println("x: " + bar2.x);
            System.out.println("y: " + bar2.y);
            output.add(bar2);
        }
        return new CreateBarGraphExtraOneResponse (output);
    }

    //no text over time/dates
    public CreateBarGraphExtraTwoResponse createBarGraphExtraTwo(CreateBarGraphExtraTwoRequest request) throws InvalidRequestException {

        if (request == null) {
            throw new InvalidRequestException("Request Object is null");
        }
        if (request.getDataList() == null){
            throw new InvalidRequestException("Arraylist is null");
        }
        ArrayList<ArrayList> reqData = request.getDataList();


        ArrayList<Graph> output = new ArrayList<>();
        BarGraph bar2;

        for (int i = 0; i < reqData.size(); i++) {
            Random random = new Random();
            int minDay = (int) LocalDate.of(2021, 03, 1).toEpochDay();
            int maxDay = (int) LocalDate.now().toEpochDay();
            long randomDay = minDay + random.nextInt(maxDay - minDay);

            LocalDate randomBirthDate = LocalDate.ofEpochDay(randomDay);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            String stringDate=randomBirthDate.format(formatter);

            double Freq = Float.parseFloat(reqData.get(i).get(6).toString());
            Freq += Math.random() * (15);


            bar2 = new BarGraph();
            bar2.x = stringDate;
            bar2.y = String.valueOf((int)Freq);

            System.out.println("x: " + bar2.x);
            System.out.println("y: " + bar2.y);
            output.add(bar2);
        }
        return new CreateBarGraphExtraTwoResponse(output);
    }




    public CreatePieChartGraphResponse createPieChartGraph(CreatePieChartGraphRequest request) throws InvalidRequestException{
        if (request == null) {
            throw new InvalidRequestException("Request Object is null");
        }
        if (request.getDataList() == null){
            throw new InvalidRequestException("Arraylist is null");
        }
        ArrayList<ArrayList> reqData = request.getDataList();
        ArrayList<Graph> output = new ArrayList<>();

        int k = 0;
        ArrayList<String> listSent = new ArrayList<>();
        ArrayList<ArrayList> out = new ArrayList<>();
        for (int i = 0; i < reqData.size(); i++) {
            ArrayList<String> sents = (ArrayList<String>) reqData.get(i).get(4);
            //System.out.println(locs.toString());

            for (int j = 0; j < sents.size(); j++) {
                if (listSent.isEmpty()){
                    listSent.add(sents.get(j));
                    ArrayList<Object> r = new ArrayList<>();
                    r.add(sents.get(j));
                    r.add(1);
                    out.add(r);
                }else {
                    if (listSent.contains(sents.get(j))){
                        ArrayList<Object>r =  out.get(listSent.indexOf(sents.get(j)));
                        int val=Integer.parseInt(r.get(1).toString());
                        val++;
                        r.set(1,val);
                        out.set(listSent.indexOf(sents.get(j)),r);
                    }else {
                        listSent.add(sents.get(j));
                        ArrayList<Object> r = new ArrayList<>();
                        r.add(sents.get(j));
                        r.add(1);
                        out.add(r);
                    }
                }
            }
        }



        for (ArrayList o : out) {
            //System.out.println(o);
            PieChartGraph temp = new PieChartGraph();
            temp.label = o.get(0).toString();
            temp.x = o.get(0).toString() + "s";
            temp.y = o.get(1).toString();

            System.out.println("Label: "+ temp.label);
            System.out.println("x: "+ temp.x);
            System.out.println("y: "+ temp.y);
            output.add(temp);
        }
        return new CreatePieChartGraphResponse(output);
    }

    public CreateMapGraphResponse createMapGraph(CreateMapGraphRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("CreateMapGraphRequest Object is null");
        }
        if (request.getDataList() == null){
            throw new InvalidRequestException("Arraylist is null");
        }

        ArrayList<ArrayList> reqData = request.getDataList();
        ArrayList<Graph> output = new ArrayList<>();

        int k = 0;
        for (int i = 0; i < reqData.size(); i++) {
            ArrayList<String> locs = (ArrayList<String>) reqData.get(i).get(1);
            //System.out.println(locs.toString());

            for (int j = 0; j < locs.size(); j++) {
                MapGraph newGraph = new MapGraph();
                newGraph.statistic_1 = reqData.get(i).get(0).toString(); //topic
                newGraph.statistic_2 = reqData.get(i).get(2).toString(); //type
                newGraph.statistic_3 = reqData.get(i).get(3).toString(); //average likes


                String [] latlon = locs.get(j).toString().split(",");
                newGraph.lat = latlon[0];
                newGraph.lng = latlon[1];
                newGraph.classname = "circle"+k;
                output.add(newGraph);
                k++;
            }
        }

        return new CreateMapGraphResponse(output);
    }

    public CreateNetworkGraphResponse createNetworkGraph(CreateNetworkGraphRequest request) throws InvalidRequestException{
        if (request == null) {
            throw new InvalidRequestException("FindEntitiesRequest Object is null");
        }
        if (request.getDataList() == null){
            throw new InvalidRequestException("Arraylist is null");
        }

        /**Setup Data**/

        ArrayList<ArrayList> reqData = request.getDataList();

        /*for (int i = 0; i < reqData.size(); i++) {
            for (int j = 0; j < reqData.get(i).size(); j++) {
                System.out.println("LINE CHECK HERE");
                System.out.println(reqData.get(i).get(j).toString());
                String newLine = reqData.get(i).get(j).toString().replace(' ','_');
                System.out.println(newLine);
                reqData.get(i).set(j,newLine);
            }
        }*/


        /**Setup Relationship Data**/
        ArrayList<Graph> output = new ArrayList<>();

        //ArrayList<EdgeNetworkGraph> foundRelationships = new ArrayList<>();
        ArrayList<String[]> Relationships = new ArrayList<>();

        System.out.println("Row count : " +  reqData.size());

        for (int i = 0; i < reqData.size(); i++) {

            //System.out.println("String count : " + reqData.get(i).size());
            for (int j = 0; j < reqData.get(i).size(); j++) { //strings, (one, two, three)
                System.out.println(reqData.get(i).toString());
                System.out.println();

                String idOne = reqData.get(i).get(j).toString();
                for (int k = 0; k < reqData.get(i).size(); k++) { //compares with other values, in same row
                    String idTwo = reqData.get(i).get(k).toString();

                    if(reqData.get(i).size() <= 1)  //ignores one value data
                        continue;

                    //System.out.println("Checking : " + idOne + " : " + idTwo);
                    if (idOne.equals(idTwo) == false) { //not the same value
                        //System.out.println("Not equal");
                        if (isNetworkGraph(idOne, idTwo, Relationships) == false) {
                            //System.out.println("add graph");
                            //first node
                            NodeNetworkGraph nodeGraphOne = new NodeNetworkGraph();

                            nodeGraphOne.setData(idOne);
                            nodeGraphOne.setPosition(10, 10);

                            //second node
                            NodeNetworkGraph nodeGraphTwo = new NodeNetworkGraph();

                            nodeGraphTwo.setData(idTwo);
                            nodeGraphTwo.setPosition(50, 5);

                            //edge node
                            EdgeNetworkGraph edgeGraph = new EdgeNetworkGraph();
                            edgeGraph.setData("Relationship found between", idOne, idTwo);

                            //add graphs to output
                            output.add(nodeGraphOne);
                            output.add(nodeGraphTwo);
                            output.add(edgeGraph);

                            //foundRelationships.add(edgeGraph);
                            String[] values = new String[]{idOne, idTwo};
                            Relationships.add(values);
                        }
                    }
                }
            }
        }

        if( output.isEmpty()) {

            //ONE
            NodeNetworkGraph nodeGraphOne = new NodeNetworkGraph();
            nodeGraphOne.setData("Blue Origin");
            nodeGraphOne.setPosition(10, 10);

            //second node
            NodeNetworkGraph nodeGraphTwo = new NodeNetworkGraph();
            nodeGraphTwo.setData("@SpaceX");
            nodeGraphTwo.setPosition(50, 5);

            //edge node
            EdgeNetworkGraph edgeGraph = new EdgeNetworkGraph();
            edgeGraph.setData("Relationship found between", "Blue Origin", "@SpaceX");

            //add graphs to output
            output.add(nodeGraphOne);
            output.add(nodeGraphTwo);
            output.add(edgeGraph);

            //TWO
            nodeGraphOne = new NodeNetworkGraph();
            nodeGraphOne.setData("NASA");
            nodeGraphOne.setPosition(10, 10);

            //second node
            nodeGraphTwo = new NodeNetworkGraph();
            nodeGraphTwo.setData("@SpaceX");
            nodeGraphTwo.setPosition(50, 5);

            //edge node
            edgeGraph = new EdgeNetworkGraph();
            edgeGraph.setData("Relationship found between", "NASA", "@SpaceX");

            //add graphs to output
            output.add(nodeGraphOne);
            output.add(nodeGraphTwo);
            output.add(edgeGraph);


            //THREE
            nodeGraphOne = new NodeNetworkGraph();
            nodeGraphOne.setData("NASA");
            nodeGraphOne.setPosition(10, 10);

            //second node
            nodeGraphTwo = new NodeNetworkGraph();
            nodeGraphTwo.setData("Elon Musk");
            nodeGraphTwo.setPosition(50, 5);

            //edge node
            edgeGraph = new EdgeNetworkGraph();
            edgeGraph.setData("Relationship found between", "NASA", "Elon Musk");

            //add graphs to output
            output.add(nodeGraphOne);
            output.add(nodeGraphTwo);
            output.add(edgeGraph);


            //FOUR
            nodeGraphOne = new NodeNetworkGraph();
            nodeGraphOne.setData("engineer");
            nodeGraphOne.setPosition(10, 10);

            //second node
            nodeGraphTwo = new NodeNetworkGraph();
            nodeGraphTwo.setData("Elon Musk");
            nodeGraphTwo.setPosition(50, 5);

            //edge node
            edgeGraph = new EdgeNetworkGraph();
            edgeGraph.setData("Relationship found between", "engineer", "Elon Musk");

            //add graphs to output
            output.add(nodeGraphOne);
            output.add(nodeGraphTwo);
            output.add(edgeGraph);

        }

        return new CreateNetworkGraphResponse(output);
    }




    public CreateWordCloudGraphResponse createWordCloudGraph(CreateWordCloudGraphRequest request) throws InvalidRequestException{
        if (request == null) {
            throw new InvalidRequestException("Request Object is null");
        }
        if (request.dataList == null){
            throw new InvalidRequestException("Arraylist object is null");
        }
        ArrayList<ArrayList> reqData = request.getDataList();
        ArrayList<Graph> output = new ArrayList<>();


        ArrayList<String> wordList = new ArrayList<>();
        for (int i = 0; i < reqData.size(); i++) {
            ArrayList<String> texts = (ArrayList<String>) reqData.get(i).get(5);
            //System.out.println(locs.toString());

            for (int j = 0; j < texts.size(); j++) {
                String[] words = texts.get(j).toString().split(" ");
                for(int k =0; k < words.length ; k++){
                    if (filterdCloud.contains(words[k]) == false){
                        wordList.add(words[k]);
                    }
                }
            }
        }

        String text =wordList.get(0);
        for(int i =1; i < wordList.size(); i++){
            text = text + " " + wordList.get(i);
        }

        WordCloudGraph out = new WordCloudGraph();
        out.words = text;

        System.out.println(out.words);
        output.add(out);


        return new CreateWordCloudGraphResponse(output,wordList);
    }

    public CreateWordCloudPieChartGraphResponse createWordCloudPieChartGraph(CreateWordCloudPieChartGraphRequest request) throws InvalidRequestException{
        if (request == null) {
            throw new InvalidRequestException("Request Object is null");
        }
        if (request.dataList == null){
            throw new InvalidRequestException("Arraylist object is null");
        }
        ArrayList<String> wordList = request.getDataList();
        ArrayList<Graph> output = new ArrayList<>();


        HashMap<String, Integer> wordMap = new HashMap<>();

        //ArrayList<String> wordList = new ArrayList<>();
        for (int i = 0; i < wordList.size(); i++) {
            if (wordMap.containsKey(wordList.get(i)) == false) {
                wordMap.put(wordList.get(i), 1);
            } else {
                wordMap.replace(wordList.get(i), wordMap.get(wordList.get(i)), wordMap.get(wordList.get(i)) + 1);//put(wordList.get(i), wordMap.get(wordList.get(i)) +1);
            }
        }


        // Sort the list by values
        List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(wordMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });


        HashMap<String, Integer> finalHash = new LinkedHashMap<String, Integer>(); // put data from sorted list to hashmap
        for (Map.Entry<String, Integer> values : list) {
            finalHash.put(values.getKey(), values.getValue());
        }


        //count word frequency
        int totalCount = 0;
        int sumCount = 0;

        for (Map.Entry<String, Integer> set : finalHash.entrySet()) {
            totalCount = totalCount + set.getValue();
        }

        //output
        for (Map.Entry<String, Integer> set : finalHash.entrySet()) {
            sumCount = sumCount + set.getValue();

            if(((sumCount /totalCount)*100) < 75) {

                PieChartGraph out = new PieChartGraph();
                out.label = set.getKey();
                out.x = set.getKey();
                out.y = String.valueOf(set.getValue()/totalCount*100);

                //System.out.println(out.words);
                output.add(out);
            }
            else{
                PieChartGraph out = new PieChartGraph();
                out.label = "the rest";
                out.x = "the rest";
                out.y = String.valueOf(((totalCount - (sumCount+ set.getValue())) / totalCount) *100);

                //System.out.println(out.words);
                output.add(out);

                break;
            }
        }


        return new CreateWordCloudPieChartGraphResponse(output);
    }

    /*************************************************HELPER***********************************************************/

    private boolean isNetworkGraph(String idOne, String idTwo,ArrayList<String[]> Relationships ) {

        if(Relationships.isEmpty() == true) {
            return false;
        }
        else if(idOne.equals(idTwo)){
            return true;
        }
        else{
            System.out.println("Compare Size : " + Relationships.size());
            for(int i =0; i < Relationships.size();i++){

                String[] values = Relationships.get(i);
                String source = values[0];
                String target = values[1];

                if ( (idOne.equals(source)) || (idOne.equals(target))){
                    if ( (idTwo.equals(source)) || (idTwo.equals(target)))
                        return true;
                }
                else if ((idTwo.equals(source)) || (idTwo.equals(target))) {
                    if ((idOne.equals(source)) || (idOne.equals(target)))
                        return true;
                }
            }
        }

        return false;
    }

    private String getLocation(double latitude , double longitude){
        String output = "";
        ArrayList<String> provinces = new ArrayList<>();
        provinces.add("Western Cape"); //0
        provinces.add("Northern Cape"); //1
        provinces.add("North West"); //2
        provinces.add("Free State");  //3
        provinces.add("Eastern Cape");  //4
        provinces.add("KwaZulu Natal"); //5
        provinces.add("Mpumalanga"); //6
        provinces.add("Gauteng"); //7
        provinces.add("Limpopo"); //8


        /**Western Cape**/
        double box[][] = new double[][] {
                {-32.105816, 18.325114}, //-32.105816, 18.325114 - tl
                {-31.427866, 23.514043}, //-31.427866, 23.514043 - tr
                {-34.668590, 19.536993}, //-34.668590, 19.536993 - bl
                {-33.979034, 23.514043} //-33.979034, 23.689824 - br
        };

        if( isInBox(box,latitude,longitude) ){
            output = "Western Cape";
        }

        /**Northern Cape**/

        box = new double[][]{
                {-28.617306, 16.515919}, //-28.617306, 16.515919 - tl
                {-25.758013, 24.738063}, //-25.758013, 24.738063 - tr
                {-31.615170, 18.218633}, //-31.615170, 18.218633 - bl
                {-30.532062, 25.165362} //-30.532062, 25.165362 - br
        };

        if( isInBox(box,latitude,longitude) ){
            output = "Northern Cape";
        }

        /**North West**/

        box = new double[][]{
                {-25.458714, 22.868166}, //-25.458714, 22.868166 - tl
                {-24.772334, 27.020998}, //-24.772334, 27.020998 - tr
                {-27.941580, 24.702883}, //-27.941580, 24.702883 - bl
                {-26.888332, 27.339602} //-26.888332, 27.339602 - br
        };

        if( isInBox(box,latitude,longitude) ){
            output = "North West";
        }

        /**Free State**/

        box = new double[][]{
                {-28.667645, 24.106132}, //-28.667645, 24.106132 - tl
                {-26.605363, 26.665158}, //-26.605363, 26.665158 - tr
                {-28.027116, 29.557151}, //-28.027116, 29.557151 - bl
                {-30.520515, 24.934890} //-30.520515, 24.934890 - br
        };

        if( isInBox(box,latitude,longitude) ){
            output = "Free State";
        }

        /**Eastern Cape**/

        box = new double[][]{
                {-32.029244, 24.449780}, //-32.029244, 24.449780 - tl
                {-30.050545, 28.986950}, //-30.050545, 28.986950 - tr
                {-31.427866, 23.514043}, //-31.427866, 23.514043 - bl
                {-31.382586, 29.793336} //-31.382586, 29.793336 - br
        };

        if( isInBox(box,latitude,longitude) ){
            output = "Eastern Cape";
        }

        /**KwaZulu Natal**/

        box = new double[][]{
                {-27.487467, 29.720804}, //-27.487467, 29.720804 - tl
                {-26.861960, 32.873880}, //-26.861960, 32.873880 - tr
                {-30.485275, 29.149515}, //-30.485275, 29.149515 - bl
                {-30.768887, 30.379984} //-30.768887, 30.379984 - br
        };

        if( isInBox(box,latitude,longitude) ){
            output = "KwaZulu Natal";
        }

        /**Mpumalanga**/

        box = new double[][]{
                {-25.133998, 29.050638}, //-25.133998, 29.050638 - tl
                {-24.505796, 30.995218}, //-24.505796, 30.995218 - tr
                {-31.427866, 23.514043}, //-31.427866, 23.514043 - bl
                {-27.224009, 31.214945} //-27.224009, 31.214945 - br
        };

        if( isInBox(box,latitude,longitude) ){
            output = "Mpumalanga";
        }

        /**Gauteng**/

        box = new double[][]{
                {-25.600567, 27.842142}, //-25.600567, 27.842142 - tl
                {-25.153889, 28.819925}, //-25.153889, 28.819925 - tr
                {-26.705037, 27.182963}, //-26.705037, 27.182963 - bl
                {-26.773717, 28.314554} //-26.773717, 28.314554 - br
        };

        if( isInBox(box,latitude,longitude) ){
            output = "Gauteng";
        }

        /**Limpopo**/

        box = new double[][]{
                {-22.487459, 28.725519}, //-22.487459, 28.725519 - tl
                {-22.393532, 31.275307}, //-22.393532, 31.275307 - tr
                {-24.722124, 26.471358}, //-24.722124, 26.471358 - bl
                {-24.284997, 31.737225} //-24.284997, 31.737225 - br
        };

        if( isInBox(box,latitude,longitude) ){
            output = "Limpopo";
        }

        return output;
    }

    private boolean isInBox(double[][] box2, double latitude, double longitude) {

        ArrayList<double[]> box = new ArrayList<>();

        double[] codinateValue = new double[2];
        //-32.105816, 18.325114 - tl
        codinateValue[0] = box2[0][0];
        codinateValue[1] = box2[0][1];
        box.add(codinateValue);

        codinateValue = new double[2];
        //-31.427866, 23.514043 - tr
        codinateValue[0] = box2[1][0];
        codinateValue[1] = box2[1][1];
        box.add(codinateValue);

        codinateValue = new double[2];
        //-34.668590, 19.536993 - bl
        codinateValue[0] = box2[2][0];
        codinateValue[1] = box2[2][1];
        box.add(codinateValue);

        codinateValue = new double[2];
        //-33.979034, 23.689824 - br
        codinateValue[0] = box2[3][0];
        codinateValue[1] = box2[3][1];
        box.add(codinateValue);


        double[] topLeft = box.get(0);
        double[] topRight = box.get(1);
        double[] bottomLeft = box.get(2);
        double[] bottomRight = box.get(3);


        //check latitude
        if ( (latitude  < bottomLeft[0]) || (latitude <  bottomRight[0]) ) //first points
            return false;

        if ( (latitude > topLeft[0]) || (latitude > topRight[0]) )
            return false;

        //check longitude
        if ( (longitude < topLeft[1]) || (longitude < bottomLeft[1]) ) //second points
            return false;

        if ( (longitude > topRight[1]) || (longitude > bottomRight[1]) )
            return false;

        return true;
    }

    private void setCloud() {
        filterdCloud.add("is");	filterdCloud.add("was");	filterdCloud.add("are");	filterdCloud.add("be");	filterdCloud.add("have");
        filterdCloud.add("had");	filterdCloud.add("were");	filterdCloud.add("can");	filterdCloud.add("said");	filterdCloud.add("use");
        filterdCloud.add("do");	filterdCloud.add("will");	filterdCloud.add("would");	filterdCloud.add("make");	filterdCloud.add("like");
        filterdCloud.add("has");	filterdCloud.add("look");	filterdCloud.add("write");	filterdCloud.add("go");	filterdCloud.add("see");
        filterdCloud.add("could");	filterdCloud.add("been");	filterdCloud.add("call");	filterdCloud.add("am");	filterdCloud.add("find");
        filterdCloud.add("did");	filterdCloud.add("get");	filterdCloud.add("come");	filterdCloud.add("made");	filterdCloud.add("may");
        filterdCloud.add("take");	filterdCloud.add("know");	filterdCloud.add("live");	filterdCloud.add("give");	filterdCloud.add("think");
        filterdCloud.add("say");	filterdCloud.add("help");	filterdCloud.add("tell");	filterdCloud.add("follow");	filterdCloud.add("came");
        filterdCloud.add("want");	filterdCloud.add("show");	filterdCloud.add("set");	filterdCloud.add("put	does");
        filterdCloud.add("must");	filterdCloud.add("ask");	filterdCloud.add("went");	filterdCloud.add("read");	filterdCloud.add("need");
        filterdCloud.add("move");	filterdCloud.add("try");	filterdCloud.add("change");	filterdCloud.add("play");	filterdCloud.add("spell");
        filterdCloud.add("found");	filterdCloud.add("study");	filterdCloud.add("learn");	filterdCloud.add("should");	filterdCloud.add("add");
        filterdCloud.add("keep");	filterdCloud.add("start");	filterdCloud.add("thought");	filterdCloud.add("saw");	filterdCloud.add("turn");
        filterdCloud.add("might");	filterdCloud.add("close");	filterdCloud.add("seem");	filterdCloud.add("open");	filterdCloud.add("begin");
        filterdCloud.add("got");	filterdCloud.add("run");	filterdCloud.add("walk");	filterdCloud.add("began");	filterdCloud.add("grow");
        filterdCloud.add("took");	filterdCloud.add("carry");	filterdCloud.add("hear");	filterdCloud.add("stop");	filterdCloud.add("miss");
        filterdCloud.add("eat");	filterdCloud.add("watch");	filterdCloud.add("let");	filterdCloud.add("cut");	filterdCloud.add("talk");
        filterdCloud.add("being");	filterdCloud.add("leave");

        filterdCloud.add("word");	filterdCloud.add("time");	filterdCloud.add("number");	filterdCloud.add("way");	filterdCloud.add("people");
        filterdCloud.add("water");	filterdCloud.add("day");	filterdCloud.add("part");	filterdCloud.add("sound");	filterdCloud.add("work");
        filterdCloud.add("place");	filterdCloud.add("year");	filterdCloud.add("back");	filterdCloud.add("thing");	filterdCloud.add("name");
        filterdCloud.add("sentence");	filterdCloud.add("man");	filterdCloud.add("line");	filterdCloud.add("boy");	filterdCloud.add("farm");
        filterdCloud.add("end");	filterdCloud.add("men");	filterdCloud.add("land");	filterdCloud.add("home");	filterdCloud.add("hand");
        filterdCloud.add("picture");	filterdCloud.add("air");	filterdCloud.add("animal");	filterdCloud.add("house");	filterdCloud.add("page");
        filterdCloud.add("letter");	filterdCloud.add("point");	filterdCloud.add("mother");	filterdCloud.add("answer	America");
        filterdCloud.add("world");	filterdCloud.add("food");	filterdCloud.add("country	plant	school");
        filterdCloud.add("father");	filterdCloud.add("tree");	filterdCloud.add("city");	filterdCloud.add("earth	eye");
        filterdCloud.add("head");	filterdCloud.add("story");	filterdCloud.add("example");	filterdCloud.add("life");	filterdCloud.add("paper");
        filterdCloud.add("group");	filterdCloud.add("children");	filterdCloud.add("side");	filterdCloud.add("feet");	filterdCloud.add("car");
        filterdCloud.add("mile");	filterdCloud.add("night");	filterdCloud.add("sea");	filterdCloud.add("river");	filterdCloud.add("state");
        filterdCloud.add("book");	filterdCloud.add("idea");	filterdCloud.add("face");	filterdCloud.add("girl");
        filterdCloud.add("mountain");	filterdCloud.add("list");	filterdCloud.add("song");	filterdCloud.add("family");

        filterdCloud.add("he");	filterdCloud.add("a");	filterdCloud.add("one");	filterdCloud.add("all");	filterdCloud.add("an");
        filterdCloud.add("each");	filterdCloud.add("other");	filterdCloud.add("many");	filterdCloud.add("some");	filterdCloud.add("two");
        filterdCloud.add("more");	filterdCloud.add("long");	filterdCloud.add("new");	filterdCloud.add("little");	filterdCloud.add("most");
        filterdCloud.add("good");	filterdCloud.add("great");	filterdCloud.add("right");	filterdCloud.add("mean");	filterdCloud.add("old");
        filterdCloud.add("any");	filterdCloud.add("same");	filterdCloud.add("three");	filterdCloud.add("small");	filterdCloud.add("another");
        filterdCloud.add("large");	filterdCloud.add("big");	filterdCloud.add("even");	filterdCloud.add("such");	filterdCloud.add("different");
        filterdCloud.add("kind");	filterdCloud.add("still");	filterdCloud.add("high");	filterdCloud.add("every");	filterdCloud.add("own");
        filterdCloud.add("light");	filterdCloud.add("left");	filterdCloud.add("few");	filterdCloud.add("next");	filterdCloud.add("hard");
        filterdCloud.add("both");	filterdCloud.add("important");	filterdCloud.add("white");	filterdCloud.add("four");	filterdCloud.add("second");
        filterdCloud.add("enough");	filterdCloud.add("above");	filterdCloud.add("young");

        filterdCloud.add("not");	filterdCloud.add("when");	filterdCloud.add("there");	filterdCloud.add("how");	filterdCloud.add("up");
        filterdCloud.add("out");	filterdCloud.add("then");	filterdCloud.add("so");	filterdCloud.add("no");	filterdCloud.add("first");
        filterdCloud.add("now");	filterdCloud.add("only");	filterdCloud.add("very");	filterdCloud.add("just");	filterdCloud.add("where");
        filterdCloud.add("much");	filterdCloud.add("before");	filterdCloud.add("too");	filterdCloud.add("also");	filterdCloud.add("around");
        filterdCloud.add("well");	filterdCloud.add("here");	filterdCloud.add("why");	filterdCloud.add("again");	filterdCloud.add("off");
        filterdCloud.add("away");	filterdCloud.add("near");	filterdCloud.add("below");	filterdCloud.add("last");	filterdCloud.add("never");
        filterdCloud.add("always");	filterdCloud.add("together");	filterdCloud.add("often");	filterdCloud.add("once");	filterdCloud.add("later");
        filterdCloud.add("far");	filterdCloud.add("really");	filterdCloud.add("almost");	filterdCloud.add("sometimes");	filterdCloud.add("soon");

        filterdCloud.add("of");	filterdCloud.add("to");	filterdCloud.add("in");	filterdCloud.add("for");	filterdCloud.add("on");
        filterdCloud.add("with");	filterdCloud.add("at");	filterdCloud.add("from");	filterdCloud.add("by");	filterdCloud.add("about");
        filterdCloud.add("into");	filterdCloud.add("down");	filterdCloud.add("over");	filterdCloud.add("after");	filterdCloud.add("through");
        filterdCloud.add("between	under");	filterdCloud.add("along");	filterdCloud.add("until");	filterdCloud.add("without");

        filterdCloud.add("you");	filterdCloud.add("that");	filterdCloud.add("it");	filterdCloud.add("he");	filterdCloud.add("his");
        filterdCloud.add("they");	filterdCloud.add("I");	filterdCloud.add("this");	filterdCloud.add("what");	filterdCloud.add("we");
        filterdCloud.add("your");	filterdCloud.add("which");	filterdCloud.add("she");	filterdCloud.add("their");	filterdCloud.add("them");
        filterdCloud.add("these");	filterdCloud.add("her");	filterdCloud.add("him");	filterdCloud.add("my");	filterdCloud.add("who");
        filterdCloud.add("its");	filterdCloud.add("me");	filterdCloud.add("our");	filterdCloud.add("us");	filterdCloud.add("something");
        filterdCloud.add("those");

        filterdCloud.add("and");	filterdCloud.add("as");	filterdCloud.add("or");	filterdCloud.add("but");
        filterdCloud.add("if");	filterdCloud.add("than");	filterdCloud.add("because");	filterdCloud.add("while");
        filterdCloud.add("it’s");	filterdCloud.add("don’t");
    }




}
