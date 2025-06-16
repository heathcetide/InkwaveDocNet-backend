package org.cetide.hibiscus.infrastructure.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.cetide.hibiscus.common.exception.BusinessException;
import org.cetide.hibiscus.infrastructure.netty.protocol.MessageType;
import org.cetide.hibiscus.infrastructure.netty.protocol.NettyMessage;
import org.cetide.hibiscus.infrastructure.netty.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.cetide.hibiscus.domain.model.enums.ResponseCodeEnum.VALIDATION_ERROR;
import static org.cetide.hibiscus.infrastructure.netty.protocol.MessageType.*;

public class ServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    private final SessionManager sessionManager = new SessionManager();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        NettyMessage message = NettyMessage.fromJsonString(text);
        System.out.print(message);
        try {
            handleMessage(channelHandlerContext, message);
        } catch (Exception e) {
            log.error("处理消息失败: " + e.getMessage());
            channelHandlerContext.writeAndFlush(new TextWebSocketFrame("错误: " + e.getMessage()));
            throw new BusinessException(VALIDATION_ERROR.code(), "处理消息失败: " + e.getMessage());
        }
    }

    private void handleMessage(ChannelHandlerContext ctx, NettyMessage message) {
        switch (message.getType()) {
            case DOCUMENT_JOIN:
                System.out.println(DOCUMENT_JOIN.getDescription());
                handleDocumentJoin(ctx, message);
                break;
            case DOCUMENT_LEAVE:
                System.out.println(DOCUMENT_LEAVE.getDescription());
//                handleDocumentLeave(message);
                break;
            case MOUSE_MOVE:
                System.out.println(MOUSE_MOVE.getDescription());
                handleMouseMove(message);
                break;
            case CONTENT_INSERT:
                System.out.println(CONTENT_INSERT.getDescription());
                handleContentInsert(message);
                break;
            case CONTENT_DELETE:
                System.out.println(CONTENT_DELETE.getDescription());
//                handleContentDelete(message);
                break;
            case CURSOR_MOVE:
                System.out.println(CURSOR_MOVE.getDescription());
//                handleCursorMove(message);
                break;
            case UNDO:
                System.out.println(UNDO.getDescription());
//                handleUndo(message);
                break;
            case REDO:
                System.out.println(REDO.getDescription());
//                handleRedo(message);
                break;
            case PERMISSION_CHANGE:
                System.out.println(PERMISSION_CHANGE.getDescription());
//                handlePermissionChange(message);
                break;
            case SAVE_VERSION:
                System.out.println(SAVE_VERSION.getDescription());
//                handleSaveVersion(message);
                break;
            case LOAD_VERSION:
                System.out.println(LOAD_VERSION.getDescription());
//                handleLoadVersion(message);
                break;
            case LOCK:
                System.out.println(LOCK.getDescription());
//                handleLock(message);
                break;
            case UNLOCK:
                System.out.println(UNLOCK.getDescription());
//                handleUnlock(message);
                break;
            case CHUNK_REQUEST:
                System.out.println(CHUNK_REQUEST.getDescription());
//                handleChunkRequest(message);
                break;
            case HEARTBEAT:
                System.out.println(HEARTBEAT.getDescription());
                break;
            default:
                sendError(message.getUserId(), "未知的消息类型");
        }
    }

    private void handleMouseMove(NettyMessage message) {
        // 检查读权限 permissionService.checkPermission

        NettyMessage notice = new NettyMessage.Builder()
                .type(MOUSE_MOVE)
                .docId(message.getDocId())
                .userId(message.getUserId())
                .build();
        notice.getPayload().put("position", message.getPayload().get("position"));
        sessionManager.broadcastToDocument(message.getDocId(), notice.toJsonString());
    }

    /**
     * 处理内容插入
     */
    private void handleContentInsert(NettyMessage message) {
        // 检查写权限 permissionService.checkPermission

        int position = (int) message.getPayload().get("position");
        String content = (String) message.getPayload().get("content");

        // 更新文档内容
//        String docContent = documentService.getDocument(message.getDocId());
//        String newContent = docContent.substring(0, position) + content + docContent.substring(position);
//        documentService.updateDocument(message.getDocId(), newContent);
        // 广播内容插入
        NettyMessage notice = new NettyMessage.Builder()
                .type(CONTENT_INSERT)
                .docId(message.getDocId())
                .userId(message.getUserId())
                .build();
        notice.getPayload().put("position", position);
        notice.getPayload().put("content", content);
        sessionManager.broadcastToDocument(message.getDocId(), notice.toJsonString());
    }

    /**
     * 处理用户加入文档
     */
    private void handleDocumentJoin(ChannelHandlerContext ctx, NettyMessage message) {
        // 检查读权限 Permission Service

        // 添加用户会话
        sessionManager.addSession(message.getUserId(), message.getDocId(), ctx.channel());


        // 广播新用户上线通知
        NettyMessage notice = new NettyMessage.Builder()
                .type(USER_ONLINE)
                .docId(message.getDocId())
                .userId("system")
                .timestamp(System.currentTimeMillis())
                .build();
        notice.getPayload().put("userId", message.getUserId());
        sessionManager.broadcastToDocument(message.getDocId(), notice.toJsonString());

        List<String> onlineUsers = sessionManager.getOnlineUsers(message.getDocId());
        onlineUsers.remove(message.getUserId()); // 排除自己

        NettyMessage userListNotice = new NettyMessage.Builder()
                .type(USER_ONLINE)
                .docId(message.getDocId())
                .userId("system")
                .timestamp(System.currentTimeMillis())
                .build();

        // payload里放整个在线用户列表
        userListNotice.getPayload().put("users", onlineUsers);
        // 一次性发送给新用户
        sessionManager.broadcastToUser(message.getUserId(), userListNotice.toJsonString());
    }

    private void sendError(String userId, String errorMessage) {
        try {
            System.out.println("发送错误消息给用户: " + userId);
            System.err.println("发送错误消息失败: " + errorMessage);
            // 构造错误消息
            NettyMessage error = new NettyMessage.Builder()
                    .type(ERROR)
                    .userId("system")
                    .timestamp(System.currentTimeMillis())
                    .build();
            error.getPayload().put("message", errorMessage);

            // 发送错误消息给指定用户
            sessionManager.broadcastToUser(userId, error.toJsonString());
        } catch (Exception e) {
            System.err.println("发送错误消息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
