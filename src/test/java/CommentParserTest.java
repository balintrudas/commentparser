import environment.c.CustomAnnotation;
import com.github.balintrudas.commentparser.CommentStore;
import com.github.balintrudas.commentparser.Scanner;
import com.github.balintrudas.commentparser.configuration.CommentMarkerConfiguration;
import com.github.balintrudas.commentparser.configuration.Configuration;
import com.github.balintrudas.commentparser.configuration.GroupMarkerConfiguration;
import com.github.balintrudas.commentparser.marker.MarkerElement;
import com.github.balintrudas.commentparser.marker.CommentElement;
import com.github.balintrudas.commentparser.util.CommentElementUtil;
import environment.e.SimpleCustomAnnotation;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;

public class CommentParserTest {

    @Test
    public void shouldReturnAllComment() throws IOException {
        //GIVEN
        CommentMarkerConfiguration commentMarkerConfiguration = new CommentMarkerConfiguration()
                .toBuilder()
                .includeWithoutMarker(true)
                .includeOnlyWithinGroup(false)
                .includeOnlyWithinMethods(false)
                .build();

        GroupMarkerConfiguration groupMarkerConfiguration = new GroupMarkerConfiguration();

        Configuration configuration = new Configuration().
                toBuilder()
                .baseDirs(Arrays.asList(CommentParserTestUtil.getEnvironmentPath()))
                .commentMarkerConfiguration(commentMarkerConfiguration)
                .groupMarkerConfiguration(groupMarkerConfiguration)
                .build();

        //WHEN
        Scanner scanner = new Scanner(configuration);

        //THEN
        CommentStore commentStore = scanner.parse();

        Assert.assertNotNull(commentStore);
        Assert.assertNotNull(commentStore.getComments());
        Assert.assertTrue(!commentStore.getComments().isEmpty());

        Assert.assertTrue(commentStore.getComments().containsKey(""));
        Assert.assertTrue(commentStore.getComments().containsKey("BAMethodGroup"));
        Assert.assertTrue(commentStore.getComments().containsKey("AClassGroup"));
        Assert.assertTrue(commentStore.getComments().containsKey("BMethodGroup"));

        LinkedHashSet<CommentElement> group = commentStore.getComments().get("");
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "aClass_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "bMethod_02_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "bClass_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "bClass_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "bClass_03"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "cMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "dMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "dMethod_02_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "dMethodGroup02_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "dMethodGroup02_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "CCustomAnnotationGroup_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "E2SimpleCustomAnnotation"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "e2Method_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "ESimpleCustomAnnotation"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(group, "eMethod_01_01"));


        Assert.assertEquals(15, group.size());

        LinkedHashSet<CommentElement> bAMethodGroup = commentStore.getComments().get("BAMethodGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(bAMethodGroup, "baMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bAMethodGroup, "baMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bAMethodGroup, "baClass_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bAMethodGroup, "baClass_02"));
        Assert.assertEquals(4, bAMethodGroup.size());

        LinkedHashSet<CommentElement> aClassGroup = commentStore.getComments().get("AClassGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_01_03"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_02_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_03"));
        Assert.assertEquals(7, aClassGroup.size());

        LinkedHashSet<CommentElement> bMethodGroup = commentStore.getComments().get("BMethodGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_03"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_01_03"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_02_01"));
        Assert.assertEquals(7, bMethodGroup.size());

    }

    @Test
    public void shouldReturnOnlyWithinGroupComment() throws IOException {
        //GIVEN
        CommentMarkerConfiguration commentMarkerConfiguration = new CommentMarkerConfiguration()
                .toBuilder()
                .includeWithoutMarker(true)
                .includeOnlyWithinGroup(true)
                .includeOnlyWithinMethods(false)
                .build();

        GroupMarkerConfiguration groupMarkerConfiguration = new GroupMarkerConfiguration();

        Configuration configuration = new Configuration().
                toBuilder()
                .baseDirs(Arrays.asList(CommentParserTestUtil.getEnvironmentPath()))
                .commentMarkerConfiguration(commentMarkerConfiguration)
                .groupMarkerConfiguration(groupMarkerConfiguration)
                .build();

        //WHEN
        Scanner scanner = new Scanner(configuration);

        //THEN
        CommentStore commentStore = scanner.parse();

        Assert.assertNotNull(commentStore);
        Assert.assertNotNull(commentStore.getComments());
        Assert.assertTrue(!commentStore.getComments().isEmpty());

        Assert.assertTrue(commentStore.getComments().containsKey("BAMethodGroup"));
        Assert.assertTrue(commentStore.getComments().containsKey("AClassGroup"));
        Assert.assertTrue(commentStore.getComments().containsKey("BMethodGroup"));

        LinkedHashSet<CommentElement> bAMethodGroup = commentStore.getComments().get("BAMethodGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(bAMethodGroup, "baMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bAMethodGroup, "baMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bAMethodGroup, "baClass_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bAMethodGroup, "baClass_02"));
        Assert.assertEquals(4, bAMethodGroup.size());

        LinkedHashSet<CommentElement> aClassGroup = commentStore.getComments().get("AClassGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_01_03"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_02_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_03"));
        Assert.assertEquals(7, aClassGroup.size());

        LinkedHashSet<CommentElement> bMethodGroup = commentStore.getComments().get("BMethodGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_03"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_01_03"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_02_01"));
        Assert.assertEquals(7, bMethodGroup.size());

    }

    @Test
    public void shouldReturnOnlyWithinGroupWithMarkerComment() throws IOException {
        //GIVEN
        CommentMarkerConfiguration commentMarkerConfiguration = new CommentMarkerConfiguration()
                .toBuilder()
                .includeWithoutMarker(false)
                .includeOnlyWithinGroup(true)
                .includeOnlyWithinMethods(false)
                .build();

        GroupMarkerConfiguration groupMarkerConfiguration = new GroupMarkerConfiguration();

        Configuration configuration = new Configuration().
                toBuilder()
                .baseDirs(Arrays.asList(CommentParserTestUtil.getEnvironmentPath()))
                .commentMarkerConfiguration(commentMarkerConfiguration)
                .groupMarkerConfiguration(groupMarkerConfiguration)
                .build();

        //WHEN
        Scanner scanner = new Scanner(configuration);

        //THEN
        CommentStore commentStore = scanner.parse();

        Assert.assertNotNull(commentStore);
        Assert.assertNotNull(commentStore.getComments());
        Assert.assertTrue(!commentStore.getComments().isEmpty());

        Assert.assertTrue(commentStore.getComments().containsKey("BAMethodGroup"));
        Assert.assertTrue(commentStore.getComments().containsKey("AClassGroup"));
        Assert.assertTrue(commentStore.getComments().containsKey("BMethodGroup"));

        LinkedHashSet<CommentElement> bAMethodGroup = commentStore.getComments().get("BAMethodGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(bAMethodGroup, "baMethod_01_02"));
        Assert.assertEquals(1, bAMethodGroup.size());

        LinkedHashSet<CommentElement> aClassGroup = commentStore.getComments().get("AClassGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_01_03"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_02_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_03"));
        Assert.assertEquals(6, aClassGroup.size());

        LinkedHashSet<CommentElement> bMethodGroup = commentStore.getComments().get("BMethodGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_03"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_01_03"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_02_01"));
        Assert.assertEquals(6, bMethodGroup.size());

    }


    @Test
    public void shouldReturnWithMarkerWithGroupWithMethodComment() throws IOException {
        //GIVEN
        CommentMarkerConfiguration commentMarkerConfiguration = new CommentMarkerConfiguration()
                .toBuilder()
                .includeWithoutMarker(false)
                .includeOnlyWithinGroup(true)
                .includeOnlyWithinMethods(true)
                .build();

        GroupMarkerConfiguration groupMarkerConfiguration = new GroupMarkerConfiguration();

        Configuration configuration = new Configuration().
                toBuilder()
                .baseDirs(Arrays.asList(CommentParserTestUtil.getEnvironmentPath()))
                .commentMarkerConfiguration(commentMarkerConfiguration)
                .groupMarkerConfiguration(groupMarkerConfiguration)
                .build();

        //WHEN
        Scanner scanner = new Scanner(configuration);

        //THEN
        CommentStore commentStore = scanner.parse();

        Assert.assertNotNull(commentStore);
        Assert.assertNotNull(commentStore.getComments());
        Assert.assertTrue(!commentStore.getComments().isEmpty());

        Assert.assertTrue(commentStore.getComments().containsKey("BAMethodGroup"));
        Assert.assertTrue(commentStore.getComments().containsKey("AClassGroup"));
        Assert.assertTrue(commentStore.getComments().containsKey("BMethodGroup"));

        LinkedHashSet<CommentElement> bAMethodGroup = commentStore.getComments().get("BAMethodGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(bAMethodGroup, "baMethod_01_02"));
        Assert.assertEquals(1, bAMethodGroup.size());

        LinkedHashSet<CommentElement> aClassGroup = commentStore.getComments().get("AClassGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_02_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_01_03"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_03"));
        Assert.assertEquals(6, aClassGroup.size());

        LinkedHashSet<CommentElement> bMethodGroup = commentStore.getComments().get("BMethodGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_02"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_03"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_01_03"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "aMethod_02_01"));
        Assert.assertEquals(6, bMethodGroup.size());

    }

    @Test
    public void shouldReturnWithCustomAnnotation() throws IOException {
        //GIVEN
        CommentMarkerConfiguration commentMarkerConfiguration = new CommentMarkerConfiguration()
                .toBuilder()
                .includeWithoutMarker(false)
                .includeOnlyWithinGroup(true)
                .includeOnlyWithinMethods(true)
                .build();

        GroupMarkerConfiguration groupMarkerConfiguration = new GroupMarkerConfiguration()
                .toBuilder()
                .setAnnotations(CustomAnnotation.class, SimpleCustomAnnotation.class)
                .annotationGroupNameKey("group")
                .annotationGroupInheritKey("groupInherit")
                .build();

        Configuration configuration = new Configuration().
                toBuilder()
                .baseDirs(Arrays.asList(CommentParserTestUtil.getEnvironmentPath()))
                .commentMarkerConfiguration(commentMarkerConfiguration)
                .groupMarkerConfiguration(groupMarkerConfiguration)
                .build();

        //WHEN
        Scanner scanner = new Scanner(configuration);

        //THEN
        CommentStore commentStore = scanner.parse();

        Assert.assertNotNull(commentStore);
        Assert.assertNotNull(commentStore.getComments());
        Assert.assertTrue(!commentStore.getComments().isEmpty());

        Assert.assertTrue(commentStore.getComments().containsKey("CCustomAnnotationGroup"));

        LinkedHashSet<CommentElement> cCustomAnnotationGroup = commentStore.getComments().get("CCustomAnnotationGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(cCustomAnnotationGroup, "cMethod_01_01"));
        Assert.assertEquals(1, cCustomAnnotationGroup.size());

    }

    @Test
    public void shouldReturnWithSimpleCustomAnnotation() throws IOException {
        //GIVEN
        CommentMarkerConfiguration commentMarkerConfiguration = new CommentMarkerConfiguration()
                .toBuilder()
                .includeWithoutMarker(false)
                .includeOnlyWithinGroup(true)
                .includeOnlyWithinMethods(true)
                .build();

        GroupMarkerConfiguration groupMarkerConfiguration = new GroupMarkerConfiguration()
                .toBuilder()
                .setAnnotations(SimpleCustomAnnotation.class)
                .build();

        Configuration configuration = new Configuration().
                toBuilder()
                .baseDirs(Arrays.asList(CommentParserTestUtil.getEnvironmentPath()))
                .commentMarkerConfiguration(commentMarkerConfiguration)
                .groupMarkerConfiguration(groupMarkerConfiguration)
                .build();

        //WHEN
        Scanner scanner = new Scanner(configuration);

        //THEN
        CommentStore commentStore = scanner.parse();

        Assert.assertNotNull(commentStore);
        Assert.assertNotNull(commentStore.getComments());
        Assert.assertTrue(!commentStore.getComments().isEmpty());

        Assert.assertTrue(commentStore.getComments().containsKey("E2SimpleCustomAnnotation"));

        LinkedHashSet<CommentElement> e2SimpleCustomAnnotation = commentStore.getComments().get("E2SimpleCustomAnnotation");
        Assert.assertTrue(CommentParserTestUtil.hasComment(e2SimpleCustomAnnotation, "e2Method_01_01"));
        Assert.assertEquals(1, e2SimpleCustomAnnotation.size());

        Assert.assertTrue(commentStore.getComments().containsKey("E2SimpleCustomAnnotation"));

        LinkedHashSet<CommentElement> eSimpleCustomAnnotation = commentStore.getComments().get("ESimpleCustomAnnotation");
        Assert.assertTrue(CommentParserTestUtil.hasComment(eSimpleCustomAnnotation, "eMethod_01_01"));
        Assert.assertEquals(1, eSimpleCustomAnnotation.size());

    }

    @Test
    public void shouldReturnWithCustomTagWithDescriptions() throws IOException {
        //GIVEN
        CommentMarkerConfiguration commentMarkerConfiguration = new CommentMarkerConfiguration()
                .toBuilder()
                .includeWithoutMarker(false)
                .includeOnlyWithinGroup(true)
                .includeOnlyWithinMethods(true)
                .build();

        GroupMarkerConfiguration groupMarkerConfiguration = new GroupMarkerConfiguration()
                .toBuilder()
                .setAnnotations(CustomAnnotation.class)
                .annotationGroupNameKey("group")
                .annotationGroupInheritKey("groupInherit")
                .setCommentGroupTags("customTag")
                .addCommentGroupInheritTag("customInheritTag")
                .build();

        Configuration configuration = new Configuration().
                toBuilder()
                .baseDirs(Arrays.asList(CommentParserTestUtil.getEnvironmentPath()))
                .commentMarkerConfiguration(commentMarkerConfiguration)
                .groupMarkerConfiguration(groupMarkerConfiguration)
                .build();

        //WHEN
        Scanner scanner = new Scanner(configuration);

        //THEN
        CommentStore commentStore = scanner.parse();

        Optional<MarkerElement> DMethodGroup01Marker = CommentElementUtil.getMarkerElementForGroup("DMethodGroup01", commentStore.getComments().get("DMethodGroup01"));
        Optional<MarkerElement> DMethodGroup02Marker = CommentElementUtil.getMarkerElementForGroup("DMethodGroup02", commentStore.getComments().get("DMethodGroup02"));
        Optional<MarkerElement> CCustomAnnotationGroupMarker = CommentElementUtil.getMarkerElementForGroup("CCustomAnnotationGroup", commentStore.getComments().get("CCustomAnnotationGroup"));

        Assert.assertNotNull(commentStore);
        Assert.assertNotNull(commentStore.getComments());
        Assert.assertTrue(!commentStore.getComments().isEmpty());

        Assert.assertTrue(commentStore.getComments().containsKey("DMethodGroup01"));
        Assert.assertTrue(commentStore.getComments().containsKey("DMethodGroup02"));

        LinkedHashSet<CommentElement> dMethodGroup01 = commentStore.getComments().get("DMethodGroup01");
        Assert.assertTrue(CommentParserTestUtil.hasComment(dMethodGroup01, "dMethod_01_01"));
        Assert.assertEquals(1, dMethodGroup01.size());

        LinkedHashSet<CommentElement> dMethodGroup02 = commentStore.getComments().get("DMethodGroup02");
        Assert.assertTrue(CommentParserTestUtil.hasComment(dMethodGroup02, "dMethod_02_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(dMethodGroup02, "cMethod_01_01"));
        Assert.assertEquals(2, dMethodGroup02.size());

        Assert.assertTrue(DMethodGroup01Marker.isPresent());
        Assert.assertNotNull(DMethodGroup01Marker.get().getDescription());
        Assert.assertTrue(!DMethodGroup01Marker.get().getDescription().isEmpty());
        Assert.assertEquals(DMethodGroup01Marker.get().getDescription(), "dMethodGroup01_01");

        Assert.assertTrue(DMethodGroup02Marker.isPresent());
        Assert.assertNotNull(DMethodGroup02Marker.get().getDescription());
        Assert.assertTrue(!DMethodGroup02Marker.get().getDescription().isEmpty());
        Assert.assertEquals(DMethodGroup02Marker.get().getDescription(), "dMethodGroup02_01");

        Assert.assertTrue(CCustomAnnotationGroupMarker.isPresent());
        Assert.assertNotNull(CCustomAnnotationGroupMarker.get().getDescription());
        Assert.assertTrue(!CCustomAnnotationGroupMarker.get().getDescription().isEmpty());
        Assert.assertEquals(CCustomAnnotationGroupMarker.get().getDescription(), "CCustomAnnotationGroup_01_01");

    }

    @Test
    public void shouldReturnEmptyStore() throws IOException {
        //GIVEN
        CommentMarkerConfiguration commentMarkerConfiguration = new CommentMarkerConfiguration()
                .toBuilder()
                .enableStartWith(false)
                .enableContains(false)
                .enableTags(false)
                .build();

        Configuration configuration = new Configuration().
                toBuilder()
                .baseDirs(Arrays.asList(CommentParserTestUtil.getEnvironmentPath()))
                .commentMarkerConfiguration(commentMarkerConfiguration)
                .build();

        //WHEN
        Scanner scanner = new Scanner(configuration);

        //THEN
        CommentStore commentStore = scanner.parse();

        Assert.assertNotNull(commentStore);
        Assert.assertNotNull(commentStore.getComments());
        Assert.assertTrue(commentStore.getComments().isEmpty());

    }

    @Test
    public void shouldReturnOnlyStartWithComments() throws IOException {
        //GIVEN
        CommentMarkerConfiguration commentMarkerConfiguration = new CommentMarkerConfiguration()
                .toBuilder()
                .enableStartWith(true)
                .enableContains(false)
                .enableTags(false)
                .build();

        Configuration configuration = new Configuration().
                toBuilder()
                .baseDirs(Arrays.asList(CommentParserTestUtil.getEnvironmentPath()))
                .commentMarkerConfiguration(commentMarkerConfiguration)
                .build();

        //WHEN
        Scanner scanner = new Scanner(configuration);

        //THEN
        CommentStore commentStore = scanner.parse();

        Assert.assertNotNull(commentStore);
        Assert.assertNotNull(commentStore.getComments());
        Assert.assertTrue(!commentStore.getComments().isEmpty());

        Assert.assertTrue(commentStore.getComments().containsKey("BMethodGroup"));
        Assert.assertTrue(commentStore.getComments().containsKey("BAMethodGroup"));

        LinkedHashSet<CommentElement> bMethodGroup = commentStore.getComments().get("BMethodGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(bMethodGroup, "bMethod_01_03"));
        Assert.assertEquals(2, bMethodGroup.size());

        LinkedHashSet<CommentElement> baMethodGroup = commentStore.getComments().get("BAMethodGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(baMethodGroup, "baMethod_01_02"));
        Assert.assertEquals(1, baMethodGroup.size());
    }

    @Test
    public void shouldReturnOnlyContainsComments() throws IOException {
        //GIVEN
        CommentMarkerConfiguration commentMarkerConfiguration = new CommentMarkerConfiguration()
                .toBuilder()
                .enableStartWith(false)
                .enableContains(true)
                .enableTags(false)
                .build();

        Configuration configuration = new Configuration().
                toBuilder()
                .baseDirs(Arrays.asList(CommentParserTestUtil.getEnvironmentPath()))
                .commentMarkerConfiguration(commentMarkerConfiguration)
                .build();

        //WHEN
        Scanner scanner = new Scanner(configuration);

        //THEN
        CommentStore commentStore = scanner.parse();

        Assert.assertNotNull(commentStore);
        Assert.assertNotNull(commentStore.getComments());
        Assert.assertTrue(!commentStore.getComments().isEmpty());

        Assert.assertTrue(commentStore.getComments().containsKey("AClassGroup"));
        Assert.assertTrue(commentStore.getComments().containsKey("BMethodGroup"));

        LinkedHashSet<CommentElement> aClassGroup = commentStore.getComments().get("AClassGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_02_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_02"));
        Assert.assertEquals(3, aClassGroup.size());

        LinkedHashSet<CommentElement> bMethodGroup = commentStore.getComments().get("BMethodGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_01_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_02_01"));
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "bMethod_01_02"));
        Assert.assertEquals(3, bMethodGroup.size());

    }

    @Test
    public void shouldReturnOnlyTagsComments() throws IOException {
        //GIVEN
        CommentMarkerConfiguration commentMarkerConfiguration = new CommentMarkerConfiguration()
                .toBuilder()
                .enableStartWith(false)
                .enableContains(false)
                .enableTags(true)
                .build();

        Configuration configuration = new Configuration().
                toBuilder()
                .baseDirs(Arrays.asList(CommentParserTestUtil.getEnvironmentPath()))
                .commentMarkerConfiguration(commentMarkerConfiguration)
                .build();

        //WHEN
        Scanner scanner = new Scanner(configuration);

        //THEN
        CommentStore commentStore = scanner.parse();

        Assert.assertNotNull(commentStore);
        Assert.assertNotNull(commentStore.getComments());
        Assert.assertTrue(!commentStore.getComments().isEmpty());

        Assert.assertTrue(commentStore.getComments().containsKey("AClassGroup"));

        LinkedHashSet<CommentElement> aClassGroup = commentStore.getComments().get("AClassGroup");
        Assert.assertTrue(CommentParserTestUtil.hasComment(aClassGroup, "aMethod_01_03"));
        Assert.assertEquals(1, aClassGroup.size());

    }
}